package com.arron_yeoman.maths;
// package com.arronyeoman.maths;

 public class Functions {

    public static void rotate(Vector2 vector, float angleDeg, Vector2 origin){
        float x = vector.x - origin.x;
        float y = vector.y - origin.y;
        float angleRad = (float) Math.toRadians(angleDeg);

        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        float xNew = x * cos - y * sin;
        float yNew = x * sin + y * cos;

        vector.x = xNew + origin.x;
        vector.y = yNew + origin.y;
    }

    public static boolean compare(float a, float b, float epsilon){
        return Math.abs(a - b) < epsilon * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b)));
    }

    public static boolean compare(Vector2 v1, Vector2 v2, float epsilon){
        return compare(v1.x, v2.x, epsilon) && compare(v1.y, v2.y, epsilon);
    }

    // Default epsilon value of 1 millionth
    public static boolean compare(float a, float b){
        return Math.abs(a - b) < (1E-6) * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b)));
    }

    public static boolean compare(Vector2 v1, Vector2 v2){
        return compare(v1.x, v2.x) && compare(v1.y, v2.y);
    }
}