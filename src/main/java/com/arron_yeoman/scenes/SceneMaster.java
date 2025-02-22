package com.arron_yeoman.scenes;

import com.arron_yeoman.engine.Camera;

public abstract class SceneMaster {

    public SceneMaster() {
    }

    public abstract void init();

    public abstract void update(float dt);

    public abstract Camera getCamera();

    public abstract void setGameOver();

    
}
