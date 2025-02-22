package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class AABB extends Collider2D{
    private Vector2 center = new Vector2();
    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private RigidBody2D rigidBody = null;
    
    public AABB(){
    }


    public AABB(Vector2 min, Vector2 max, Vector2 position){
        this.size = new Vector2(max).subtract(min);
        this.halfSize = new Vector2(size).multiplyScalar(0.5f);
        this.center = position;
    }

    public Vector2 getCenter(){
        return this.center;
    }

    public Vector2 getSize(){
        return this.size;
    }

    public void setSize(Vector2 size){
        this.size = size;
        this.halfSize = new Vector2(size).multiplyScalar(0.5f);

    }

    public Vector2 getMin(){
        return new Vector2(this.rigidBody.getPosition()).subtract(new Vector2(halfSize));
    }

    public Vector2 getMax(){
        return new Vector2(this.rigidBody.getPosition()).add(new Vector2(halfSize));
    }

    public void setRigidBody(RigidBody2D rb){
        this.rigidBody = rb;
    }

    public void setOffset(Vector2 offset){
        this.offset = new Vector2(rigidBody.getPosition()).add(offset);
    }

}
