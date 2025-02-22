package com.arron_yeoman.physics2d.primitives;

import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.maths.Vector4;

public class Line2D {
    private Vector2 start = new Vector2();
    private Vector2 end = new Vector2();
    private Vector4 color = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);


    public Line2D(Vector2 start, Vector2 end){
        this.start = start;
        this.end = end;
    }

    public Line2D(Vector2 start, Vector2 end, Vector4 color){
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }

    public Vector2 getEnd() {
        return end;
    }

    public void setEnd(Vector2 end) {
        this.end = end;
    }

    public float lengthSquared(){
        return new Vector2(this.end).subtract(this.start).lengthSquared();
    }

    public Vector4 getColor() {
        return color;
    }

    public void setColor(Vector4 color) {
        this.color = color;
    }

    

}
