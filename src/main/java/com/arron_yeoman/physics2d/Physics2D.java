package com.arron_yeoman.physics2d;


import java.util.ArrayList;
import java.util.List;

import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.observer.EventSystem;
import com.arron_yeoman.observer.Observer;
import com.arron_yeoman.observer.events.Event;
import com.arron_yeoman.observer.events.EventEnumerator;
import com.arron_yeoman.physics2d.force2D.ForceGenerator;
import com.arron_yeoman.physics2d.force2D.ForceRegistry;
import com.arron_yeoman.physics2d.force2D.Gravity2D;
import com.arron_yeoman.physics2d.primitives.Collider2D;
import com.arron_yeoman.physics2d.rigidBody2d.CollisionManifold;
import com.arron_yeoman.physics2d.rigidBody2d.Collisions;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;

public class Physics2D implements Observer{

    private ForceRegistry registry;
    private List<ForceGenerator> generators;
    private float fixedDT;
    private Gravity2D gravity;


    private List<RigidBody2D> bodies;
    private List<RigidBody2D> collidingBodies1;
    private List<RigidBody2D> collidingBodies2;
    private List<CollisionManifold> collisions;
    private int maxIterations = 10;

    private boolean suspended = false;


    public Physics2D(float fixedDT, Vector2 gravity){
        EventSystem.addObserver(this);

        this.registry = new ForceRegistry();
        this.bodies = new ArrayList<>();
        this.collidingBodies1 = new ArrayList<>();
        this.collidingBodies2 = new ArrayList<>();
        this.collisions = new ArrayList<>();
        this.gravity = new Gravity2D(gravity);
        this.fixedDT = fixedDT;

    }

    public void update(float dt){
        if(!suspended){
            fixedUpdate();
        }
    }

    public void fixedUpdate(){
        //clear all lists
        collidingBodies1.clear();
        collidingBodies2.clear();
        collisions.clear();

        //find collisons
        int size = bodies.size();
        for(int i = 0; i < size; i++){
            for(int j = i; j < size; j++){
                if (i == j)
                {
                    continue;
                }   

                CollisionManifold cm = new CollisionManifold();
                RigidBody2D rb1 = bodies.get(i);
                RigidBody2D rb2 = bodies.get(j);

                Collider2D c1 = rb1.getCollider();
                Collider2D c2 = rb2.getCollider();

                if (c1 != null && c2 != null && !(rb1.isImmovable() && rb2.isImmovable()))
                {
                    cm = Collisions.findCollisionInfo(c1, c2);
                    //System.out.println(cm.isColliding());
                }
                if (cm != null && cm.isColliding()){
                    //System.out.println("Collision detected");
                    collidingBodies1.add(rb1);
                    collidingBodies2.add(rb2);
                    collisions.add(cm);
                }
            }
        }
                
        //update forces
        registry.updateForces(fixedDT);

        //resolve collisions
        for (int i = 0; i < maxIterations; i++){
            for (int j = 0; j < collisions.size(); j++){
                int steps = collisions.get(j).getContacts().size();
                for (int k = 0; k < steps; k++){
                    RigidBody2D rb1 = collidingBodies1.get(j);
                    RigidBody2D rb2 = collidingBodies2.get(j);

                    applyImpulse(rb1, rb2, collisions.get(j));
                }
            }
        }

        //update physics
        for(RigidBody2D rb : bodies){
            rb.physUpdate(fixedDT);
        }

        
    }

    private void applyImpulse(RigidBody2D rb1, RigidBody2D rb2, CollisionManifold cm) {
        float invMass1 = rb1.getInverseMass();
        float invMass2 = rb2.getInverseMass();
        float totalInvMass = invMass1 + invMass2;
    
        if (totalInvMass == 0) {
            return;
        }
    
        Vector2 relativeSpeed = new Vector2(rb2.getLinearVelocity()).subtract(rb1.getLinearVelocity());
        Vector2 relativeNormal = new Vector2(cm.getNormal()).normalise();
    
        if ("Player".equals(rb1.gameObject.getName()) || "Player".equals(rb2.gameObject.getName())) {
            if (relativeNormal.y < -0.5f) {
                EventSystem.notify(null, new Event(EventEnumerator.PLAYER_LAND));
            }
        }
    
        if (relativeSpeed.dot(relativeNormal) > 0f) {
            return;
        }
    
        float e = Math.min(rb1.getRestitution(), rb2.getRestitution());
        float numerator = -(1 + e) * relativeSpeed.dot(relativeNormal);
        float j = numerator / totalInvMass;
    
        if (!cm.getContacts().isEmpty() && j != 0f) {
            j /= (float) cm.getContacts().size();
        }
    
        Vector2 impulse = new Vector2(relativeNormal).multiplyScalar(j);
    
        rb1.setLinearVelocity(rb1.getLinearVelocity().add(new Vector2(
            impulse).multiplyScalar(invMass1).multiplyScalar(-1f)));
    
        rb2.setLinearVelocity(rb2.getLinearVelocity().add(new Vector2(
            impulse).multiplyScalar(invMass2).multiplyScalar(1f)));
    
        // Positional correction to prevent sinking
        positionalCorrection(rb1, rb2, cm);
    }

    private void positionalCorrection(RigidBody2D rb1, RigidBody2D rb2, CollisionManifold cm) {
        float percent = 0.2f; // usually 20% to 80%
        float slop = 0.01f; // usually 0.01 to 0.1
        Vector2 correction = new Vector2(cm.getNormal()).multiplyScalar(
            Math.max(cm.getPenetration() - slop, 0.0f) / (rb1.getInverseMass() + rb2.getInverseMass()) * percent);
    
        rb1.setTransform(rb1.getPosition().subtract(correction.multiplyScalar(rb1.getInverseMass())));
        rb2.setTransform(rb2.getPosition().add(correction.multiplyScalar(rb2.getInverseMass())));
    }

    public void addRigidBody(RigidBody2D rb, boolean addGravity){
        rb.start();
        bodies.add(rb);
        //register gravity
        if (addGravity){
            //System.out.println("Adding gravity to registry");
            this.registry.add(rb, gravity);
        }
        
    }

    @Override
    public void onNotify(GameObject go,Event event){
        if (event.type == EventEnumerator.END_PLAY){
            System.out.println("pausing");
            this.suspended = true;
        }
        if (event.type == EventEnumerator.START_PLAY){
            System.out.println("resuming");
            this.suspended = false;
        }
            
    }
}
