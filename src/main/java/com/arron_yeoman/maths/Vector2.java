package com.arron_yeoman.maths;

public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other){
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2 add(Vector2 other){
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 subtract(Vector2 other){
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 divide(Vector2 other){
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2 multiply(Vector2 other){
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public float lengthSquared(){
        float result = this.x * this.x + this.y * this.y;
        return result;
    }

    public Vector2 multiplyScalar(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float dot(Vector2 other){
        return this.x * other.x + this.y * other.y;
    }

    public float length(){
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }


    public float cross(Vector2 other){
        return this.x * other.y - this.y * other.x;
    }

    public Vector2 normalise(){
        float mag = this.length();
        this.x /= mag;
        this.y /= mag;
        return this;
    }

    public Vector2 getUnit(){
        return this.normalise();
    }

    //maybe add conditional returns for the cases of theta = 0/180 degrees and 90/360 degrees
    public float getTheta(Vector2 other){
        return (float) Math.acos(this.dot(other) / (this.length() * other.length()));
    }

    public float get(int i){
        if(i == 0){
            return this.x;
        } else if(i == 1){
            return this.y;
        } else {
            return 0;
        }
    }

    public void set(int i, float value){
        if(i == 0){
            this.x = value;
        } else if(i == 1){
            this.y = value;
        }
    }




    public void set (float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 other){
        this.x = other.x;
        this.y = other.y;
    }

    public void printString(){
        System.out.println("Vector2: " + this.x + ", " + this.y);
    }
}
