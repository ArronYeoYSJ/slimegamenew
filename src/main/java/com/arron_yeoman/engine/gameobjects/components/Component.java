package com.arron_yeoman.engine.gameobjects.components;

import com.arron_yeoman.engine.gameobjects.GameObject;

public abstract class Component {

    public GameObject gameObject = null;
    
    public abstract void update(float dt);

    public void start(){

        
    }
}
