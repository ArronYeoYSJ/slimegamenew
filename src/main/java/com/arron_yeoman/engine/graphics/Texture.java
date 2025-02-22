package com.arron_yeoman.engine.graphics;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.arron_yeoman.engine.io.ResourceLoader;

public class Texture {

    private static int width;
    private static int height;

    public static float[] loadTexture(String textureName) throws IOException {
        ByteBuffer buffer;
        //System.out.println("Loading texture: "+textureName);
        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1); 
           // URL url = Texture.class.getResource("resources/textures/"+textureName);
            
            //String filePath = "C:\\Users\\arron\\Desktop\\Development\\slimegame\\src\\resources\\textures\\" +textureName;
            String filePath = ResourceLoader.resourcesTextureString(textureName);
            //System.out.println("File: " + textureName);
            //System.out.println("Channels " + channels.get(0));
            buffer = STBImage.stbi_load(filePath, w, h, channels, 4);
            if(buffer ==null) {
                throw new IOException("Can't load file "+textureName+" "+STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get(); 
                
            int id = GL11.glGenTextures();
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
            // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 0);
                
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            STBImage.stbi_image_free(buffer);
            return new float[]{id, width, height};
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
