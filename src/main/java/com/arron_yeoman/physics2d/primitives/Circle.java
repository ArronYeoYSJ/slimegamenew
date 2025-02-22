package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class Circle extends Collider2D {

    private Vector2 localOffset;
    private float radius = 1.0f;
    private RigidBody2D rigidBody = null;

    public Circle(float radius){
        this.radius = radius;
        this.localOffset = new Vector2();
    }

    public Circle(float radius, Vector2 offset){
        this.radius = radius;
        this.localOffset = offset;
    }

    public float getRadius(){
        return this.radius;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }

    public void setLocalOffset(Vector2 offset){
        this.localOffset.set(offset);
    }

    public Vector2 getCenter(){
        return this.rigidBody.getPosition(); 
    }
    public void setRigidBody(RigidBody2D rb){
        this.rigidBody = rb;
    }
}
