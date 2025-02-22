package com.arron_yeoman.engine.gameobjects.components;



import com.arron_yeoman.engine.Camera;
import com.arron_yeoman.engine.Window;
import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.engine.io.KBInput;
import com.arron_yeoman.engine.io.KeyActions;
import com.arron_yeoman.engine.io.KeyBinds;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.observer.EventSystem;
import com.arron_yeoman.observer.Observer;
import com.arron_yeoman.observer.events.Event;
import com.arron_yeoman.observer.events.EventEnumerator;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;


public class PlayerController extends  Component implements Observer{

    public float walkSpeed = 300f;
    public float runSpeed = 35f;
    public float jumpForce = 6000f;
    public float jumpMultiplier = 1f;
    public float jumpMultiplierMax = 1.5f;

    public float friction = 200f;

    
    public boolean isGrounded = true;
    public boolean isJumping = false;
    public boolean isRunning = false;

    public boolean isStomping = false;
    private float stompSpeed = 500f;
    private float stompTime = 0.25f;
    private float stompTimer = 0f;
    
    private float jumpAfterFallTime = 0.1f;
    private float jumpAfterFallTimer = 0f;
    
    private RigidBody2D rb;
    
    private Vector2 velocity;
    private Vector2 acceleration;
    private Vector2 maxVelocity = new Vector2(400f, 550f);

    public boolean isDead = false;

    private Camera camera;
    private float camXBound = 200;


    public PlayerController(){
    }

    @Override
    public void start() {
        this.rb = this.gameObject.getComponent(RigidBody2D.class);
        this.velocity = new Vector2();
        this.acceleration = new Vector2();  
        EventSystem.addObserver(this);
        this.camera = Window.get().getCurrentScene().getCamera();  
        this.camera.setPosition(new Vector2(0,0));   
    }

    @Override
    public void update(float dt) {
        if (isDead){
            return;
        }
        boolean right = KBInput.isKeyPressed(KeyBinds.getKeyBind(KeyActions.RIGHT));
        boolean left = KBInput.isKeyPressed(KeyBinds.getKeyBind(KeyActions.LEFT));
        boolean down = KBInput.isKeyPressed(KeyBinds.getKeyBind(KeyActions.DOWN));
        boolean jump = KBInput.isKeyPressed(KeyBinds.getKeyBind(KeyActions.JUMP));
       

        //moveCamera();

        if (this.velocity.x > 0) {
            this.velocity.x -= friction * dt * (1 + (this.velocity.x / this.maxVelocity.x));
            if (this.velocity.x < 0) {
                this.velocity.x = 0;
            }
        } else if (this.velocity.x < 0) {
            this.velocity.x += friction * dt * (1 + (-this.velocity.x / this.maxVelocity.x));
            if (this.velocity.x > 0) {
                this.velocity.x = 0;
            }
        }
        if(right && !left){
            //System.out.println("Right");
            this.acceleration.x = walkSpeed;
            if (this.gameObject.transform.scale.x < 0){
                this.gameObject.transform.scale.x *= -1;
            }
        }
        if(left && !right){
            this.acceleration.x = -walkSpeed;
            if (this.gameObject.transform.scale.x > 0){
                this.gameObject.transform.scale.x *= -1;
            }
        }
        if((right && left) || (!right && !left)){
            this.acceleration.x = 0;
        }
                


        if(down && !isGrounded && !isStomping){
            isStomping = true;          

        }
        else if (jump && isGrounded){
            System.out.println("Jumping");
            if (isGrounded){
                this.acceleration.y = jumpForce;
                isJumping = true;
                isGrounded = false;
            }
        }
        else {
            this.acceleration.y = 0;
        }

        if (isStomping){
            stompTimer += dt;
            if (stompTimer < stompTime){
                isStomping = true;
                this.velocity.y = 0;
                this.velocity.x = 0;
            } 
            else{
                this.velocity.y = -stompSpeed;
            }

        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max((Math.min(this.velocity.x, this.maxVelocity.x)), -this.maxVelocity.x);
        this.velocity.y = Math.max((Math.min(this.velocity.y, this.maxVelocity.y)), -this.maxVelocity.y);
        
        //this.velocity.printString();
        this.rb.setLinearVelocity(this.velocity);
    }

    private void moveCamera(){
        Vector2 camPos = this.camera.getPosition();
        Vector2 playerPos = this.gameObject.transform.position;
        Vector2 playerToCamera = new Vector2(playerPos.x - camPos.x, playerPos.y - camPos.y);

        if (playerToCamera.x > camXBound && camPos.x <= 1000f) {
            camPos.x = playerPos.x - camXBound;
        } else if (playerToCamera.x < -camXBound && camPos.x > 0) {
            camPos.x = playerPos.x + camXBound;
        }

        // Clamp the camera's x position to a minimum value of 0
        if (camPos.x < 0) {
            camPos.x = 0;
        }

    // Update the camera's position
    this.camera.setPosition(new Vector2(camPos.x, camPos.y));
    }

    @Override
    public void onNotify(GameObject go, Event event) {
        if (event.type == EventEnumerator.PLAYER_LAND){
            isGrounded = true;
            isJumping = false;
            isStomping = false;
            stompTimer = 0f;
            //System.out.println("Player has landed");
        }
    }


}
