package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class PillCollider extends Collider2D {
    private float height;
    private float width;

    private Circle tCircle;
    private Circle bCircle;
    private AABB box;
    private RigidBody2D rigidBody = null;

    
    public PillCollider(float width, float height){
        this.height = height;
        this.width = width;
        System.out.println("Pill collider created with width: " + width + " and height: " + height);
        float heightLessRadius = height - width;
        Vector2 boxMin = new Vector2(-width/2, -heightLessRadius/2);
        Vector2 boxMax = new Vector2(width/2, heightLessRadius/2);
        this.box = new AABB(boxMin, boxMax, new Vector2(0,0));

        Vector2 topCircleOffset = new Vector2(0, heightLessRadius/2);
        topCircleOffset.printString();
        this.tCircle = new Circle(width/2, topCircleOffset);

        Vector2 bottomCircleOffset = new Vector2(0, -heightLessRadius/2);
        this.bCircle = new Circle(width/2, bottomCircleOffset);

        
    }


    public Circle getTopCircle() {
        return tCircle;
    }


    public void setTopCircle(Circle tCircle) {
        this.tCircle = tCircle;
    }


    public Circle getBottomCircle() {
        return bCircle;
    }


    public void setBottomCircle(Circle bCircle) {
        this.bCircle = bCircle;
    }


    public AABB getBox() {
        return box;
    }


    public void setBox(AABB box) {
        this.box = box;
    }

    public RigidBody2D getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(RigidBody2D rigidBody) {
        this.rigidBody = rigidBody;
        this.tCircle.setRigidBody(rigidBody);
        this.bCircle.setRigidBody(rigidBody);
        this.box.setRigidBody(rigidBody);
    }

    
}
