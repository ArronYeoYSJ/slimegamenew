package com.arron_yeoman.engine.gameobjects.components;

import com.arron_yeoman.engine.Transform;
import com.arron_yeoman.engine.graphics.Material;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.maths.Vector4;


public class SpriteRenderer extends Component{

    boolean firstRun = true;

    private Vector4 colour;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isChanged = false;
    

    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.colour = new Vector4(1,1,1,1);
        this.isChanged = true;
    }
        
    public SpriteRenderer(Vector4 colour){
        this.colour = colour;
        this.sprite = new Sprite(null);
        this.isChanged = true;
    }

    @Override
    public void update(float dt) {
        if (firstRun){
            //System.out.println("First run of sprite renderer");
            firstRun = false;
        }
        if (!this.lastTransform.equals(this.gameObject.transform)){
            if(!(this.lastTransform.scale.x == this.gameObject.transform.scale.x)){
                System.out.println("Scale changed");
                this.isChanged = true;
            }
            this.isChanged = true;
            //System.out.println("Transform changed");
            this.gameObject.transform.copy(this.lastTransform);
            isChanged = true;
        }
    }

    @Override
    public void start() {
        this.lastTransform = this.gameObject.transform.copy();
        //System.out.println("Starting sprite renderer");
    }

    public Vector4 getColour(){
        return colour;
    }

    public Material getMaterial(){
        return sprite.getMaterial();
    }

    public Vector2[] getTexCoords(){
        return sprite.getTexCoords();
    }

    public void setColour(Vector4 colour){
        this.colour.set(colour);
        this.isChanged = true;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isChanged = true;
    }

    public boolean isChanged(){
        return isChanged;
    }

    public void changeApplied(){
        this.isChanged = false;
    }
}