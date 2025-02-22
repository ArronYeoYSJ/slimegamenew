package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.engine.gameobjects.components.Component;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public abstract class Collider2D extends Component{
    protected Vector2 offset = new Vector2();




    @Override
    public void start(){

    }

    @Override
    public void update(float dt){

    }

    public void setRigidBody(RigidBody2D rb){
        
    }

}
