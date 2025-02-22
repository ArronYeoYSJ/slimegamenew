package com.arron_yeoman.engine.gameobjects;



import java.util.ArrayList;
import java.util.List;

import com.arron_yeoman.engine.Transform;
import com.arron_yeoman.engine.gameobjects.components.Component;
import com.arron_yeoman.engine.gameobjects.components.Sprite;
import com.arron_yeoman.engine.gameobjects.components.SpriteRenderer;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.physics2d.primitives.AABB;
import com.arron_yeoman.physics2d.primitives.Circle;
import com.arron_yeoman.physics2d.primitives.Collider2D;
import com.arron_yeoman.physics2d.primitives.PillCollider;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;



public class GameObject {
    
    private String name;
    private List<Component> components;
    public Transform transform;
    private int layer = 0;
    private RigidBody2D rb;
    public boolean isDynamic = false;

    public GameObject(String goName) {
        this.name = goName;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.layer = 0;
    }

    public GameObject(String goName, Transform transform, int layer) {
        this.name = goName;
        this.layer = layer;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public static GameObject createGameObject(String name, Vector2 position, Vector2 size, Sprite sprite, String colliderType, int layer, float mass, float restitution, boolean isDynamic) {
        Transform transform = new Transform(position, size);
        RigidBody2D rb = new RigidBody2D();
        rb.setRawTransform(transform);
        rb.setMass(mass);
        rb.setRestitution(restitution);
        Collider2D collider = null;

        switch(colliderType){
            case "pillcollider":
                collider = new PillCollider(size.x,size.y);
                break;
            case "AABB":
                collider = new AABB(new Vector2 (size).multiplyScalar(-0.5f), new Vector2(size).multiplyScalar(0.5f), position);
                break;
            case "circle":
                collider = new Circle(size.x);
                break;
            default:
                System.out.println("Invalid collider type");
                assert false: "Invalid collider type";
                return null;
        }

        GameObject gameObject = new GameObject(name, transform, layer);
        gameObject.addComponent(new SpriteRenderer(sprite));
        gameObject.addComponent(rb);
        gameObject.isDynamic = isDynamic;
        rb.setGameObject(gameObject);
        collider.setRigidBody(rb);
        rb.setCollider(collider);

        return gameObject;

    }

    public void start(){
        for (Component C: components){
            //System.out.println("Starting game object: " + this.name);
            C.start();
        }
    }

    public void update(float dt){
        for (Component C: components){
            C.update(dt);
        }
        // if (rb == null){
        //     rb = getComponent(RigidBody2D.class);
        // } else {
        //     this.transform.position = rb.getPosition();
        // }
    }

    public int getLayer(){
        return this.layer;
    }

    public void setLayer(int layer){
        this.layer = layer;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for (Component C: components){
            if (componentClass.isAssignableFrom(C.getClass())){
                try {
                    //System.out.println(C.getClass() + " Component found");
                    return componentClass.cast(C);
                } catch (ClassCastException e){
                    System.out.println("Error casting component");
                    return null;
                }
            }
        }
        System.out.println("Component not found");
        return null;
    }


    public void addComponent(Component component){
        this.components.add(component);
        component.gameObject = this;
        //System.out.println(component.getClass() + " component added to game object");
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (Component C: components){
            if (componentClass.isAssignableFrom(C.getClass())){
                components.remove(C);
                return;
            }
        }
    }
}
