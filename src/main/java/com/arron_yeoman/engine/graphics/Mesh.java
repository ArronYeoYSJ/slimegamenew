package com.arron_yeoman.engine.graphics;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.arron_yeoman.maths.*;

public class Mesh {
    private  Vector4[] vertices;
    private  Vector4[] colors;
    private Material material; 
    private  int[] indices;
    private float[] UVs;
    private  int  vao, pbo, ibo, cbo, tbo;
    public String textureName;


    public Mesh(Vertex[] vertices, int[] indices, Material material) {
        this.indices = indices;
        this.material = material;

        float[] uvs = new float[vertices.length * 2];
        Vector4[] verts = new Vector4[vertices.length];
        Vector4[] cols = new Vector4[vertices.length];
        // Vector4[] norms = new Vector4[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            //System.out.println("Vertex: " + i);

            float u = vertices[i].getU();
            float v = vertices[i].getV();
            uvs[i * 2] = u;
            uvs[i * 2 + 1] = v;
            verts[i] = vertices[i].getPosition();
            cols[i] = vertices[i].getcolour();
        }
        this.UVs = uvs;
        this.vertices = verts;
        this.colors = cols;
    }

    public void initMesh() {
        createMesh(vertices, indices, UVs);
    }

    private void createMesh(Vector4[] vertices, int[] indices, float[] UVs) {
        System.out.println("Creating Mesh");
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        bufferData(vertices, indices, colors, UVs);

    }


    public void updatePositions(Vector4[] vertices) {
        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 4);
        float [] positionData = new float[vertices.length * 4];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i * 4] = vertices[i].getX();
            positionData[i * 4 + 1] = vertices[i].getY();
            positionData[i * 4 + 2] = vertices[i].getZ();
            //@TODO review this, manually setting w to 1 here fixes a perspective bug but may break stuff later
            positionData[i * 4 + 3] = vertices[i].getW();
            //System.out.println("Vertex: " + i + " x: " + vertices[i].getX() + " y: " + vertices[i].getY() + " z: " + vertices[i].getZ() + " w: " + vertices[i].getW());
        }
        positionBuffer.put(positionData).flip();
        pbo = storeData(positionBuffer, 0, 4);
        System.out.println("PBO: " + pbo);
    }

    private void updateColours(Vector4[] colour) {
        //colour buffer
        FloatBuffer colourBuffer = MemoryUtil.memAllocFloat(vertices.length * 4);
        //@TODO: review if this can be replaced with func shown in 1st 2025 lecture
        float [] colourData = new float[vertices.length * 4];
        for (int i = 0; i < vertices.length; i++) {
           // System.out.println("Colour: " + colour.r + " " + colour.g + " " + colour.b + " " + colour.a);
            colourData[i * 4] = colour[i].getX();
            colourData[i * 4 + 1] = colour[i].getY();
            colourData[i * 4 + 2] = colour[i].getZ();
            colourData[i * 4 + 3] = colour[i].getW();
        }
        colourBuffer.put(colourData).flip();

        cbo = storeData(colourBuffer, 1, 4);
        System.out.println("CBO: " + cbo);
    }

    // private void updateNormals(Vector4[] normals) {
    //     FloatBuffer normalBuffer = MemoryUtil.memAllocFloat(normals.length * 4);
    //     float [] normalData = new float[normals.length * 4];
    //     for (int i = 0; i < normals.length; i++) {
    //         Vector4 normal = normals[i];
    //         normalData[i * 4] = normal.getX();
    //         normalData[i * 4 + 1] = normal.getY();
    //         normalData[i * 4 + 2] = normal.getZ();
    //         normalData[i * 4 + 3] = normal.getW();
    //     }
    //     normalBuffer.put(normalData).flip();
    //     nbo = storeData(normalBuffer, 3, 4);
    // }

    private void bufferData(Vector4[] vertices, int[] indices, Vector4[] colors, float[] UVs){
        
        updatePositions(vertices);
        updateColours(colors);      

        //texture buffer
        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(UVs.length *2);
        textureBuffer.put(UVs).flip();
        tbo = storeData(textureBuffer, 2, 2);

        //index buffer

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        //static draw expects this data wont be changed.
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        System.out.println("IBO: " + ibo);
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        //store data in the buffer
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        //static draw expects this data wont be changed.
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        //unbinding the buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;

    }

    public void destroy() {
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(cbo);
        GL15.glDeleteBuffers(tbo);
        GL30.glDeleteVertexArrays(vao);
    }

    public Vector4[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVAO() {
        return vao;
    }

    public int getVBO() {
        return pbo;
    }

    public int getIBO() {
        return ibo;
    }

    public int getCBO() {
        return cbo;
    }

    public int getTBO() {
        return tbo;
    }

    public int getTexture() {
        return material.getTextureID();
    }
    
}
