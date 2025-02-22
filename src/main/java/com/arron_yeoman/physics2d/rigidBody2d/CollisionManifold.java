package com.arron_yeoman.physics2d.rigidBody2d;

import java.util.List;
import java.util.ArrayList;

import com.arron_yeoman.maths.Vector2;

public class CollisionManifold {
    private boolean colliding;
    private float penetration;
    private Vector2 normal;
    private List<Vector2> contacts;
     

    public CollisionManifold(){
        this.penetration = 0;
        this.normal = new Vector2();
        this.contacts = new ArrayList<Vector2>();
        this.colliding = false;
    }

    public CollisionManifold(Vector2 normal, float penetration){
        this.penetration = penetration;
        this.normal = normal;
        this.contacts = new ArrayList<>();
        this.colliding = false;

    }

    public void addContact(Vector2 contact){
        this.contacts.add(contact);
    }

        

    public float getPenetration() {
        return penetration;
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    public List<Vector2> getContacts() {
        return contacts;
    }

    public void setContacts(List<Vector2> contacts) {
        this.contacts = contacts;
    }

    public boolean isColliding() {
        //System.out.println("Colliding: " + this.colliding);
        return this.colliding;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
}
