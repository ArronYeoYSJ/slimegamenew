package com.arron_yeoman.physics2d.force2D;

import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class ForceRegistration {
    public ForceGenerator fg;
    public RigidBody2D rb;

    public ForceRegistration(ForceGenerator fg, RigidBody2D rb){
        this.fg = fg;
        this.rb = rb;
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other.getClass() != this.getClass()){
            return false;
        }
        

        ForceRegistration otherRegister = (ForceRegistration) other;
        return otherRegister.rb == this.rb && otherRegister.fg == this.fg;
    }
}
