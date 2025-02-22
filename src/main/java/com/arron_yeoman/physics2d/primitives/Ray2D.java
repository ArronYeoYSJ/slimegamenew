package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2;

public class Ray2D {
    private Vector2 origin;
    private Vector2 direction;

    public Ray2D(Vector2 origin, Vector2 direction){
        this.origin = origin;
        this.direction = direction;
        this.direction.normalise();
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

}
