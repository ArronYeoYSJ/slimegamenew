package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Functions;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class Box2D extends Collider2D {

    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private RigidBody2D rigidBody = null;

    public Box2D() {
        this.halfSize = new Vector2(this.size).multiplyScalar(0.5f);
    }

    public Box2D(Vector2 min, Vector2 max) {
        this.size = new Vector2(max).subtract(min);
        this.halfSize = new Vector2(this.size).multiplyScalar(0.5f);
    }


    public RigidBody2D getRB(){
        return this.rigidBody;
    }

    public void setRigidBody(RigidBody2D rb){
        this.rigidBody = rb;
    }

    public Vector2 getMin(){
        return new Vector2(this.rigidBody.getPosition()).subtract(this.halfSize);
    }

    public Vector2 getMax(){
        return new Vector2(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2 getSize(){
        return this.size;
    }

    public void setSize(Vector2 size){
        this.size = size;
        this.halfSize = new Vector2(size).multiplyScalar(0.5f);

    }

    public Vector2 getHalfSize(){
        return this.halfSize;
    }

    public Vector2[] getVertices(){
        Vector2 min = this.getMin();
        Vector2 max = this.getMax();

        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(min.x, min.y);
        vertices[1] = new Vector2(max.x, min.y);
        vertices[2] = new Vector2(max.x, max.y);
        vertices[3] = new Vector2(min.x, max.y);
        float rotation = this.rigidBody.getRotation();
        rotation = Math.round(rotation * 100) / 100;

        for( Vector2 vertex : vertices){
            if ( rotation != 0){
                Functions.rotate(vertex, rotation, this.rigidBody.getPosition());
            }
        }
        

        return vertices;
    }
}
