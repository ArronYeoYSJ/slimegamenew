package com.arron_yeoman.physics2d;

import com.arron_yeoman.maths.Functions;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.primitives.AABB;
import com.arron_yeoman.physics2d.primitives.Box2D;
import com.arron_yeoman.physics2d.primitives.Circle;
import com.arron_yeoman.physics2d.primitives.Line2D;
import com.arron_yeoman.physics2d.primitives.Ray2D;
import com.arron_yeoman.physics2d.primitives.RayCastOut;

public class IntersectDetector2D {

    //point on line detection

    //Line equation: y = mx + c

    public static boolean pointOnLine(Vector2 point, Line2D line){
        Vector2 start = line.getStart();
        Vector2 end = line.getEnd();

        float dx = end.x - start.x;
        float dy = end.y - start.y;
        
        float m = dy / dx;

        float c = end.y - m * end.x;

        return point.y == m * point.x + c;
        
    }

    public static boolean pointInCircle(Vector2 point, Circle circle){
        Vector2 center = circle.getCenter();
        Vector2 centerToPoint = new Vector2(point).subtract(center);

        //apparently this is significantly faster than using the magnitude method
        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB(Vector2 point, AABB bb){
        Vector2 min = bb.getMin();
        Vector2 max = bb.getMax();

        return point.x >= min.x && point.x <= max.x && point.y >= min.y && point.y <= max.y;
    }

    public static boolean pointInBox2D(Vector2 point, Box2D box){
        Vector2 min = box.getMin();
        Vector2 max = box.getMax();

        Vector2 localPoint = new Vector2(point);
        Functions.rotate(localPoint, box.getRB().getRotation(), box.getRB().getPosition());


        return localPoint.x >= min.x && localPoint.x <= max.x && localPoint.y >= min.y && localPoint.y <= max.y;
    }

    //line intersection detection 

    public static boolean lineAndCircle(Line2D line, Circle circle) {
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }

        Vector2 ab = new Vector2(line.getEnd()).subtract(line.getStart());

        // Project point (circle position) onto ab (line segment)
        // parameterized position t
        Vector2 circleCenter = circle.getCenter();
        Vector2 centerToLineStart = new Vector2(circleCenter).subtract(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        // Find the closest point to the line segment
        Vector2 closestPoint = new Vector2(line.getStart()).add(ab.multiplyScalar(t));

        return pointInCircle(closestPoint, circle);
    }

    public static boolean lineAndAABB(Line2D line, AABB box) {
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
            return true;
        }

        Vector2 unitVector = new Vector2(line.getEnd()).subtract(line.getStart());
        unitVector.normalise();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2 min = box.getMin();
        min.subtract(line.getStart()).multiply(unitVector);
        Vector2 max = box.getMax();
        max.subtract(line.getStart()).multiply(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }
        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t < line.lengthSquared();
    }

    public static boolean lineAndBox2D (Line2D line, Box2D box) {
        float theta = -box.getRB().getRotation();
        Vector2 center = box.getRB().getPosition();
        Vector2 start = new Vector2(line.getStart());
        Vector2 end = new Vector2(line.getEnd());
        Functions.rotate(start, theta, center);
        Functions.rotate(end, theta, center);

        Line2D rotatedLine = new Line2D(start, end);
        AABB aabb = new AABB(box.getMin(), box.getMax(), new Vector2());

        return lineAndAABB(rotatedLine, aabb);
    }

    //raycast detection

   public static boolean rayCast(Circle circle, Ray2D ray, RayCastOut result){
        RayCastOut.clear(result);

        Vector2 origin = ray.getOrigin();
        Vector2 originToCircle = new Vector2(circle.getCenter()).subtract(origin);
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();

        float a = originToCircle.dot(ray.getDirection());
        float b = originToCircleLengthSquared - a * a;
        if (radiusSquared - b < 0.0f) {
            return false;
        }

        float f = (float) Math.sqrt(radiusSquared - b);
        float t = a - f;
        if(originToCircleLengthSquared < radiusSquared){
            //ray must be in circle
            t = a + f;
        }else{
            //ray must be outside circle
            t = a - f;
        }

        if(result != null){
            Vector2 point = new Vector2(ray.getOrigin()).add(new Vector2(ray.getDirection()).multiplyScalar(t));
            Vector2 normal = new Vector2(point).subtract(circle.getCenter());
            normal.normalise();

            result.init(point, normal, t, true);
        }

        return true;
   }

   public static boolean rayCast(AABB box, Ray2D ray, RayCastOut result){

        RayCastOut.clear(result);
        Vector2 unitVector = ray.getDirection();
        unitVector.normalise();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2 min = box.getMin();
        min.subtract(ray.getOrigin()).multiply(unitVector);
        Vector2 max = box.getMax();
        max.subtract(ray.getOrigin()).multiply(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }
        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f; // && t * t < ray.getMax();
        if(!hit){
            return false;
        }

        if(result != null){
            Vector2 point = new Vector2(ray.getOrigin()).add(new Vector2(ray.getDirection()).multiplyScalar(t));
            Vector2 normal = new Vector2(ray.getOrigin()).subtract(point);
            normal.normalise();

            result.init(point, normal, t, true);
        }
        return true;
            
   }

   public static boolean rayCast(Box2D box, Ray2D ray, RayCastOut result){
        RayCastOut.clear(result);

        Vector2 hSize = box.getHalfSize();

        Vector2 xAxis = new Vector2(1.0f, 0.0f);
        Vector2 yAxis = new Vector2(0.0f, 1.0f);

        Functions.rotate(xAxis, -box.getRB().getRotation(), new Vector2());
        Functions.rotate(yAxis, -box.getRB().getRotation(), new Vector2());

        Vector2 p = new Vector2(box.getRB().getPosition()).subtract(ray.getOrigin());

        Vector2 f = new Vector2(xAxis.dot(p), yAxis.dot(p));

        Vector2 d = new Vector2(xAxis.dot(p), yAxis.dot(p));

        float[] tArray = {0f, 0f, 0f, 0f};
        for (int i=0; i<2; i++){
            if(Functions.compare(f.get(i), 0.0f)){
                if(-d.get(i) - hSize.get(i) > 0.0f || -d.get(i) + hSize.get(i) < 0.0f){
                    return false;
                }
                f.set(i, 0.00001f);
            }
            tArray[i * 2 + 0]= (d.get(i) + hSize.get(i)) / f.get(i); //tMax forthus axis
            tArray[i * 2 + 1]= (d.get(i) - hSize.get(i)) / f.get(i); //tmin
        }

        float tmin = Math.max(Math.min(tArray[0], tArray[1]), Math.min(tArray[2], tArray[3]));
        float tmax = Math.min(Math.max(tArray[0], tArray[1]), Math.max(tArray[2], tArray[3]));

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f; // && t * t < ray.getMax();
        if(!hit){
            return false;
        }

        if(result != null){
            Vector2 point = new Vector2(ray.getOrigin()).add(new Vector2(ray.getDirection()).multiplyScalar(t));
            Vector2 normal = new Vector2(ray.getOrigin()).subtract(point);
            normal.normalise();

            result.init(point, normal, t, true);
        }
        return true;

        
   }

    //circle intersection detection

    public static boolean circleAndLine(Circle circle, Line2D line){
        return lineAndCircle(line, circle);
    }


    public static boolean circleAndCircle(Circle circle1, Circle circle2){
        Vector2 center1 = circle1.getCenter();
        Vector2 center2 = circle2.getCenter();
        float radius1 = circle1.getRadius();
        float radius2 = circle2.getRadius();

        Vector2 centerToCenter = new Vector2(center2).subtract(center1);
        float radiusSum = radius1 + radius2;

        return centerToCenter.lengthSquared() <= radiusSum * radiusSum;
    }

    public static boolean circleAndAABB(Circle circle, AABB box){
        Vector2 min = box.getMin();
        Vector2 max = box.getMax();

        Vector2 center = circle.getCenter();
        float radius = circle.getRadius();

        Vector2 closestPoint = new Vector2(center);

        if(closestPoint.x < min.x){
            closestPoint.x = min.x;
        }else if(closestPoint.x > max.x){
            closestPoint.x = max.x;
        }
        if(closestPoint.y < min.y){
            closestPoint.y = min.y;
        }else if(closestPoint.y > max.y){
            closestPoint.y = max.y;
        }

        Vector2 circleToBox = new Vector2(center).subtract(closestPoint);
        return circleToBox.lengthSquared() <= radius * radius;

    }

    public static boolean circleAndBox2D(Circle circle, Box2D box){
        Vector2 min = new Vector2();
        Vector2 max = new Vector2(box.getSize());
        
        Vector2 r = new Vector2(box.getRB().getPosition());
        Functions.rotate(r, box.getRB().getRotation(), new Vector2());

        Vector2 localCirclePos = new Vector2(r).add(box.getHalfSize());

        Vector2 center = circle.getCenter();
        float radius = circle.getRadius();

        Vector2 closestPoint = new Vector2(localCirclePos);

        if(closestPoint.x < min.x){
            closestPoint.x = min.x;
        }else if(closestPoint.x > max.x){
            closestPoint.x = max.x;
        }
        if(closestPoint.y < min.y){
            closestPoint.y = min.y;
        }else if(closestPoint.y > max.y){
            closestPoint.y = max.y;
        }

        Vector2 circleToBox = new Vector2(localCirclePos).subtract(closestPoint);
        return circleToBox.lengthSquared() <= radius * radius;

    }

    public static boolean AABBandCircle(AABB box, Circle circle){
        return circleAndAABB(circle, box);
    }

    public static boolean AABBandAABB(AABB box1, AABB box2){
        Vector2 axesToTest[] = {new Vector2(1.0f, 0.0f), new Vector2(0.0f, 1.0f)};
        for(Vector2 axis : axesToTest){
            if(!overlapOnAxis(box1, box2, axis)){
                return false;
            }
        }
        return true;
    }

    public static boolean AABBandBox2D(AABB box, Box2D box2){
        Vector2 axesToTest[] = {
            new Vector2(1.0f, 0.0f), new Vector2(0.0f, 1.0f),
            new Vector2(1.0f, 0.0f), new Vector2(0.0f, 1.0f)
        };

        Functions.rotate(axesToTest[2], box2.getRB().getRotation(), new Vector2());
        Functions.rotate(axesToTest[3], box2.getRB().getRotation(), new Vector2());
        
        for(Vector2 axis : axesToTest){
            if(!overlapOnAxis(box, box2, axis)){
                return false;
            }
        }
        return true;
    }

    private static boolean overlapOnAxis(AABB rect1, AABB rect2, Vector2 axis){
        Vector2 interval1 = getInterval(rect1, axis);
        Vector2 interval2 = getInterval(rect2, axis);

        return (interval1.x <= interval2.y && interval2.x <= interval1.y);
    }

    private static boolean overlapOnAxis(AABB rect1, Box2D rect2, Vector2 axis){
        Vector2 interval1 = getInterval(rect1, axis);
        Vector2 interval2 = getInterval(rect2, axis);

        return (interval1.x <= interval2.y && interval2.x <= interval1.y);
    }

    private static boolean overlapOnAxis(Box2D rect1, AABB rect2, Vector2 axis){
        Vector2 interval1 = getInterval(rect1, axis);
        Vector2 interval2 = getInterval(rect2, axis);

        return (interval1.x <= interval2.y && interval2.x <= interval1.y);
    }

    private static boolean overlapOnAxis(Box2D rect1, Box2D rect2, Vector2 axis){
        Vector2 interval1 = getInterval(rect1, axis);
        Vector2 interval2 = getInterval(rect2, axis);

        return (interval1.x <= interval2.y && interval2.x <= interval1.y);
    }

    private static Vector2 getInterval(AABB rect, Vector2 axis){
        Vector2 result = new Vector2();

        Vector2 min = rect.getMin();
        Vector2 max = rect.getMax();

        Vector2[] vertices = {
            new Vector2(min.x, min.y),
            new Vector2(min.x, max.y),
            new Vector2(max.x, min.y),
            new Vector2(max.x, max.y)
        };

        result.x = axis.dot(vertices[0]);
        result.y = result.x;
        for( int i = 1; i < 4; i++){
            float projection = axis.dot(vertices[i]);
            if(projection < result.x){
                result.x = projection;
            }
            if(projection > result.y){
                result.y = projection;
            }
        }
        return result;
    }

    private static Vector2 getInterval(Box2D rect, Vector2 axis){
        Vector2 result = new Vector2();

        Vector2[] vertices = rect.getVertices();

        result.x = axis.dot(vertices[0]);
        result.y = result.x;
        for( int i = 1; i < 4; i++){
            float projection = axis.dot(vertices[i]);
            if(projection < result.x){
                result.x = projection;
            }
            if(projection > result.y){
                result.y = projection;
            }
        }
        return result;
    }
}
