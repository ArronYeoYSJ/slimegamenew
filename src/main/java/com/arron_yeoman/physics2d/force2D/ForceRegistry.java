package com.arron_yeoman.physics2d.force2D;

import java.util.ArrayList;
import java.util.List;

import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class ForceRegistry {
    private List<ForceRegistration> registry;

    public ForceRegistry(){
        this.registry = new ArrayList<ForceRegistration>();
    }

    public void updateForces(float dt){
        for(ForceRegistration fr : this.registry){
            fr.fg.updateForce(fr.rb, dt);
        }
    }

    public void add(RigidBody2D rb, ForceGenerator fg){
        ForceRegistration fr = new ForceRegistration(fg, rb);
        this.registry.add(fr); 
    }
    public void remove(RigidBody2D rb, ForceGenerator fg){
        ForceRegistration fr = new ForceRegistration(fg, rb);
        this.registry.remove(fr); 
    }
    public void clear(){
        this.registry.clear();
    }

    // public void zeroForces(){
    //     for(ForceRegistration fr : this.registry){
    //         fr.fg.clearAccumulators();
    //     }
    // }   
}
