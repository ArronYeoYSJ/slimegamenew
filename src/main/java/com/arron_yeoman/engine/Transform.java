package com.arron_yeoman.engine;
import com.arron_yeoman.maths.Vector2;

public class Transform {
    
    public Vector2 position;
    public Vector2 scale;
    public float rotation;

    public Transform(Vector2 position, Vector2 scale, float rotation){
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Transform(Vector2 position, Vector2 scale){
        this.position = position;
        this.scale = scale;
        this.rotation = 0;
    }

    public Transform(Vector2 position){
        this.position = position;
        this.scale = new Vector2(1,1);
        this.rotation = 0;
    }

    public Transform(){
        this.position = new Vector2();
        this.scale = new Vector2(1,1);
        this.rotation = 0;
    }

    public Transform copy(){
        return new Transform(new Vector2(this.position.x, this.position.y), new Vector2(this.scale.x, this.scale.y), this.rotation);

    }
    public void copy(Transform other){
        other.position.set(this.position);
        other.scale.set(this.scale);
        other.rotation = this.rotation;
    }

    @Override
    public boolean equals(Object other){
        if (other == null){return false;}
        if (!(other instanceof Transform)){return false;}
        Transform otherTransform = (Transform) other;
        return this.position.equals(otherTransform.position) && this.scale.equals(otherTransform.scale) && this.rotation == otherTransform.rotation;
    }

    public void setTransform(Vector2 position, Vector2 scale){
        this.position = position;
        this.scale = scale;
    }
    public void setTransform(Vector2 position){
        this.position = position;
    }


}
