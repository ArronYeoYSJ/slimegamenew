package com.arron_yeoman.physics2d.rigidBody2d;

import com.arron_yeoman.engine.Transform;
import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.engine.gameobjects.components.Component;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.primitives.Collider2D;
import com.arron_yeoman.physics2d.primitives.PillCollider;

public class RigidBody2D extends Component{

    public GameObject gameObject;
    private Collider2D collider;

    private Transform rawTransform;

    private Vector2 position = new Vector2();
    private Vector2 linearVelocity;
    private Vector2 linearAcceleration;
    private float linearDamping;
    private float angularDamping;
    private float rotation;
    private float mass;
    private float inverseMass;
    private float drag;
    private float angularVelocity;
    private float angularAcceleration;
    private Vector2 forceAccumulator;
    private float coefRestitution;

    public RigidBody2D(){
    }

    @Override
    public void start(){
        this.linearVelocity = new Vector2();
        this.linearAcceleration = new Vector2();
        this.coefRestitution = 0f;
        this.drag = 0;
        this.angularVelocity = 0;
        this.angularAcceleration = 0;
        this.linearDamping = 0;
        this.angularDamping = 0;
        this.rotation = 0;
        this.forceAccumulator = new Vector2();
    }

    @Override
    public void update(float dt){
    }

    public void physUpdate(float dt){
        //exit early if mass is 0, object is immovable
        if(this.mass == 0.0f){
            syncColliders();
            return;
        }
 
        Vector2 accel = new Vector2(forceAccumulator).multiplyScalar(this.inverseMass);
        //forceAccumulator.printString();
        clearAccumulators();
        //accel.printString();
        this.linearVelocity.add(accel.multiplyScalar(dt));

        //UPDATE LINEAR POSITION
        this.position.add(new Vector2(this.linearVelocity).multiplyScalar(dt));

        syncColliders();
        
    }

    public void syncColliders(){
        if(rawTransform != null){
            rawTransform.setTransform(this.position);
            this.gameObject.transform.setTransform(this.position);
        }
    }

    public void clearAccumulators(){
        //System.out.println("Clearing accumulators");
        this.forceAccumulator.set(0f,0f);
        //this.forceAccumulator.printString();

    }

    public void addForce(Vector2 force){
        //System.out.println("Force added");
        //force.printString();
        this.forceAccumulator.add(force);
    }

    

    //getters and setters

    public Vector2 getPosition(){
        return this.position;
    }

    public boolean isImmovable(){
        return this.mass == 0.0f;
    }

    public void setCollider(Collider2D collider){
        this.collider = collider;
    }

    public void setPillCollider(PillCollider collider){
        this.collider = collider;    
    }

    public Collider2D getCollider(){
        return this.collider;
    }

    public void setTransform(Vector2 position, float rotation){
        this.position.set(position);
        this.rotation = rotation;
    }
    
    public void setTransform(Vector2 position){
        this.position.set(position);
    }

    public Vector2 getLinearVelocity(){
        return this.linearVelocity;
    }

    public void setLinearVelocity(Vector2 velocity){
        this.linearVelocity = velocity;
    }

    public Vector2 getAcceleration(){
        return this.linearAcceleration;
    }

    public void setAcceleration(Vector2 acceleration){
        this.linearAcceleration = acceleration;
    }

    public float getRestitution(){
        return this.coefRestitution;
    }

    public void setRestitution(float r){
        this.coefRestitution = r;
    }

    public float getMass(){
        return this.mass;
    }

    public float getInverseMass(){
        return this.inverseMass;
    }

    public void setMass(float mass){
        this.mass = mass;
        if(mass != 0f){
            this.inverseMass = 1f / mass;
        }
    }

    public float getDrag(){
        return this.drag;
    }

    public void setDrag(float drag){
        this.drag = drag;
    }

    public float getAngularVelocity(){
        return this.angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity){
        this.angularVelocity = angularVelocity;
    }

    public float getAngularAcceleration(){
        return this.angularAcceleration;
    }

    public void setAngularAcceleration(float angularAcceleration){
        this.angularAcceleration = angularAcceleration;
    }

    public float getRotation() {
        
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setRawTransform(Transform rawTransform) {
        this.rawTransform = rawTransform;
        this.position = rawTransform.position;
    }

    public void setGameObject(GameObject gameObject){
        this.gameObject = gameObject;
    }   

}
