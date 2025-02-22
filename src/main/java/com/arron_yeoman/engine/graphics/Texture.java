package com.arron_yeoman.engine.graphics;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {

    private static int width;
    private static int height;
 public static float[] loadTexture(String textureName) throws IOException {
        ByteBuffer imageBuffer;
        
        // Load file from inside the JAR
        try (InputStream inputStream = Texture.class.getClassLoader().getResourceAsStream("textures/" + textureName);
             MemoryStack stack = MemoryStack.stackPush()) {
            
            if (inputStream == null) {
                throw new IOException("Texture not found: " + textureName);
            }

            // Convert InputStream to ByteBuffer
            imageBuffer = readInputStreamToByteBuffer(inputStream);

            // STBImage expects width, height, and channels to be allocated
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Load image from ByteBuffer instead of file path
            ByteBuffer buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, 4);
            if (buffer == null) {
                throw new IOException("Failed to load image: " + textureName + " - " + STBImage.stbi_failure_reason());
            }

            int width = w.get();
            int height = h.get();

            // OpenGL texture setup
            int id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 0);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            STBImage.stbi_image_free(buffer);

            return new float[]{id, width, height};
        }
    }

    private static ByteBuffer readInputStreamToByteBuffer(InputStream inputStream) throws IOException {
        ReadableByteChannel channel = Channels.newChannel(inputStream);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024 * 1024); // 4MB buffer
        while (channel.read(buffer) != -1) {}
        buffer.flip();
        return buffer;
    }
}
