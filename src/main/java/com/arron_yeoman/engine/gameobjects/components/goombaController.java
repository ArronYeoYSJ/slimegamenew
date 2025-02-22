package com.arron_yeoman.engine.gameobjects.components;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class goombaController extends Component{
    
    private RigidBody2D rb;
    
    private Vector2 velocity;
    private Vector2 acceleration;
    private Vector2 maxVelocity = new Vector2(100f, 50f);
    private float standardChangeDelay = 2f;
    private float changeDelay = 2f;
    private float changeTimer = 2f;
    private float nearWall = 300;

    public boolean isDead = false;

    public goombaController(){
    }

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.velocity = new Vector2();
        this.acceleration = new Vector2();
    }

    @Override
    public void update(float dt) {
        if(!isDead){
            changeTimer += dt;
            this.velocity = rb.getLinearVelocity();
            if(changeTimer > changeDelay){
                
                float changeSpeed = 2 * (float)Math.clamp(Math.random(), 0.25, 1) -1;
                Vector2 pos = rb.getPosition();
                if (pos.x > nearWall){
                    changeSpeed = -0.8f;
                }
                if (pos.x < -nearWall){
                    changeSpeed = 0.8f;
                }
                Vector2 newVelocity = new Vector2(maxVelocity.x * changeSpeed, 0);
                rb.setLinearVelocity(newVelocity);
                changeTimer = 0;
                changeDelay = standardChangeDelay + changeSpeed;
            }
        }
    }
}
