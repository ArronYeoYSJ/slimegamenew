package com.arron_yeoman.physics2d.rigidBody2d;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.primitives.AABB;
import com.arron_yeoman.physics2d.primitives.Circle;
import com.arron_yeoman.physics2d.primitives.Collider2D;
import com.arron_yeoman.physics2d.primitives.PillCollider;

public class Collisions {

    public static CollisionManifold findCollisionInfo(Collider2D a, Collider2D b){
        if (a instanceof Circle && b instanceof Circle){
            return findCollisionInfo((Circle)a,(Circle)b);
        }else if (a instanceof Circle && b instanceof AABB){
            return findCollisionInfo((Circle)a,(AABB)b);
        }else if (a instanceof AABB && b instanceof Circle){
            return findCollisionInfo((Circle)b,(AABB)a); 
        }else if (a instanceof PillCollider && b instanceof Circle){
            return findCollisionInfo((PillCollider)a,(Circle)b);
        }else if (a instanceof Circle && b instanceof PillCollider){
            return findCollisionInfo((PillCollider)b,(Circle)a);
        }else if (a instanceof PillCollider && b instanceof AABB){
            return findCollisionInfo((PillCollider)a,(AABB)b);
        }else if (a instanceof AABB && b instanceof PillCollider){
            return findCollisionInfo((PillCollider)b,(AABB)a);
        }else if (a instanceof PillCollider && b instanceof PillCollider){
            return findCollisionInfo((PillCollider)b,(PillCollider)a);
        }else if (a instanceof AABB && b instanceof AABB){
            return findCollisionInfo((AABB)a,(AABB)b);
        }    
        else {
            assert false : "Collision not supported between " + a.getClass().getName() + " and " + b.getClass().getName();
            return null;
        }
    }
    //this is broken
    public static CollisionManifold findCollisionInfo(PillCollider a, PillCollider b){
        Circle topCircleA = a.getTopCircle();
        Circle bottomCircleA = a.getBottomCircle();
        AABB boxA = a.getBox();
        Circle topCircleB = b.getTopCircle();
        Circle bottomCircleB = b.getBottomCircle();
        AABB boxB = b.getBox();
        CollisionManifold result = findCollisionInfo(bottomCircleA, bottomCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(bottomCircleA, topCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(bottomCircleA, boxB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(boxA, bottomCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(boxA, topCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(boxA, boxB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(topCircleA, bottomCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(topCircleA, topCircleB);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(topCircleA, boxB);
        return result;
    }

    public static CollisionManifold findCollisionInfo(PillCollider a, Circle b){
        Circle topCircle = a.getTopCircle();
        Circle bottomCircle = a.getBottomCircle();
        AABB box = a.getBox();
        CollisionManifold result = findCollisionInfo(bottomCircle, b);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(box, b);
        if(result.isColliding()){
            return result;
        }
        result = findCollisionInfo(topCircle, b);
        return result;
    }
    public static CollisionManifold findCollisionInfo(PillCollider a, AABB b){
        Circle topCircle = a.getTopCircle();
        Circle bottomCircle = a.getBottomCircle();
        AABB box = a.getBox();
        CollisionManifold result = findCollisionInfo(bottomCircle, b);
        if(result.isColliding()){
            //System.out.println("Collision detected at bottom circle of pill");
            return result;
        }
        result = findCollisionInfo(box, b);
        if(result.isColliding()){
            //System.out.println("Collision detected at box section of pill");
            return result;
        }
        result = findCollisionInfo(topCircle, b);
        if (result.isColliding()){
            //System.out.println("Collision detected at top circle of pill");
            return result;
        }
        return result;
    }
        

    public static CollisionManifold findCollisionInfo(Circle a,Circle b){

        CollisionManifold result = new CollisionManifold();
        float sumOfRadii = a.getRadius() + b.getRadius();
        Vector2 distance = new Vector2(b.getCenter()).subtract(a.getCenter());
        float distanceSquared = distance.lengthSquared();
        if(distanceSquared > sumOfRadii * sumOfRadii){
            //System.out.println("No collision detected");
            return result;
        }

        float depth = Math.abs(distance.length() - sumOfRadii) * 0.5f;
        Vector2 normal = new Vector2(distance).normalise();

        float distanceToPoint = a.getRadius() - depth;
        Vector2 contact = new Vector2(a.getCenter()).add(new Vector2(normal).multiplyScalar(distanceToPoint));
    
        result = new CollisionManifold(normal,depth);
        result.addContact(contact);
        result.setColliding(true);

        return result;
    }

    public static CollisionManifold findCollisionInfo(Circle a, AABB b){
        
        CollisionManifold result = new CollisionManifold();
        Vector2 closestPoint = new Vector2(b.getCenter());
        Vector2 circleCenter = new Vector2(a.getCenter());
        Vector2 halfSize = new Vector2(b.getSize()).multiplyScalar(0.5f);
        Vector2 distance = new Vector2(circleCenter).subtract(closestPoint);
        Vector2 clamped = new Vector2(distance);
        clamped.x = Math.clamp(clamped.x, -halfSize.x, halfSize.x);
        clamped.y = Math.clamp(clamped.y, -halfSize.y, halfSize.y);
        Vector2 normal = new Vector2(distance).subtract(clamped);
        float distanceSquared = normal.lengthSquared();
        float radiusSquared = a.getRadius() * a.getRadius();
        if(distanceSquared > radiusSquared){
            //System.out.println("No collision detected");
            return result;
        }
        //System.out.println("collision detected");
        float normalDistance = normal.length();
        if(normalDistance != 0){
            normal.multiplyScalar(-1f/ normalDistance);
        }else{
            normal = new Vector2(1,0);
        }
        float depth = a.getRadius() - normalDistance;
        Vector2 contact = new Vector2(a.getCenter()).add(new Vector2(normal).multiplyScalar(a.getRadius() - depth));
        result = new CollisionManifold(normal,depth);
        result.addContact(contact);
        result.setColliding(true);
        return result;
    }

    public static CollisionManifold findCollisionInfo(AABB a, AABB b){
        CollisionManifold result = new CollisionManifold();
        Vector2 aMin = a.getMin();
        Vector2 aMax = a.getMax();
        Vector2 bMin = b.getMin();
        Vector2 bMax = b.getMax();
        if(aMin.x > bMax.x || aMax.x < bMin.x || aMin.y > bMax.y || aMax.y < bMin.y){
            //System.out.println("No collision detected");
            return result;
        }
        //System.out.println("Collision detected");
        float xOverlap = Math.min(aMax.x, bMax.x) - Math.max(aMin.x, bMin.x);
        float yOverlap = Math.min(aMax.y, bMax.y) - Math.max(aMin.y, bMin.y);
        float depth = Math.min(xOverlap, yOverlap);
        Vector2 normal = new Vector2();
        if(xOverlap < yOverlap){
            normal = new Vector2(1,0);
            if(aMin.x > bMin.x){
                normal.multiplyScalar(-1);
            }
        }else{
            normal = new Vector2(0,1);
            if(aMin.y > bMin.y){
                normal.multiplyScalar(-1);
            }
        }
        Vector2 contact = new Vector2(a.getCenter()).add(new Vector2(normal).multiplyScalar(depth * 0.5f));
        result = new CollisionManifold(normal,depth);
        result.addContact(contact);
        result.setColliding(true);
        return result;
    }
}
