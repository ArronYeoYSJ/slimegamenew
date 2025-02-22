package com.arron_yeoman.physics2d.force2D;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class Gravity2D implements ForceGenerator {

    private Vector2 gravity = new Vector2();

    public Gravity2D(Vector2 gravity){
        this.gravity.set(gravity);
    }

    @Override
    public void updateForce(RigidBody2D rb, float dt) {
        rb.addForce(new Vector2(gravity).multiplyScalar(rb.getMass()));
        //new Vector2(gravity).multiplyScalar(rb.getMass()).printString();
    }

    @Override
    public void zeroForce(RigidBody2D rb) {
        rb.clearAccumulators();
    }

}
