package com.arron_yeoman.engine.gameobjects.components;

import com.arron_yeoman.engine.graphics.Material;
import com.arron_yeoman.maths.Vector2;

public class Sprite {

    private Material material;
    private Vector2[] texCoords;

    public Sprite(Material material){
        this.material = material;
        Vector2[] texCoords = {
            new Vector2(0,0),
            new Vector2(1,0),
            new Vector2(1,1),
            new Vector2(0,1)
        };
        this.texCoords = texCoords;
    }

    public Sprite(Material material, Vector2[] texCoords){
        this.material = material;
        this.texCoords = texCoords;
    }

    public Material getMaterial(){
        return this.material;
    }

    public Vector2[] getTexCoords(){
        return this.texCoords;
    }
}
