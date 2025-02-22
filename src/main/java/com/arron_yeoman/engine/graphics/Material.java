package com.arron_yeoman.engine.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL20;

public class Material {

    private float[] textureData;
    private int textureID;
    private float width, height;

    public Material(String path){
        try{
            textureData = Texture.loadTexture(path);
            textureID = (int) textureData[0];
            width = textureData[1];
            height = textureData[2];
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void destroy(){
        GL20.glDeleteTextures(textureID);
    }


    public int getTextureID() {
        return textureID;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
