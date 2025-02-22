package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2; 

public class RayCastOut {
    private Vector2 point;
    private Vector2 normal;
    private float t;
    private boolean hit;

    public RayCastOut(){
        this.point = new Vector2();
        this.normal = new Vector2();
        this.t = -1f;
        this.hit = false;
    }

    public void init(Vector2 point, Vector2 normal, float t, boolean hit){
        this.point = point;
        this.normal = normal;
        this.t = t;
        this.hit = hit;
    }

    public static void clear(RayCastOut out){
        if(out != null){
            out.point.set(0, 0);
            out.normal.set(0, 0);
            out.t = -1f;
            out.hit = false;
        }
    }

}
