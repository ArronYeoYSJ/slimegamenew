package com.arron_yeoman.physics2d.force2D;

import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public interface ForceGenerator {
    void updateForce(RigidBody2D rb, float dt);

    void zeroForce(RigidBody2D rb);
    

}
