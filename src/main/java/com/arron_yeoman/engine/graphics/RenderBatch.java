package com.arron_yeoman.engine.graphics;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import com.arron_yeoman.engine.Camera;
import com.arron_yeoman.engine.Window;
import com.arron_yeoman.engine.gameobjects.components.SpriteRenderer;
import com.arron_yeoman.maths.Matrix4x4;
import com.arron_yeoman.maths.Vector4;
import com.arron_yeoman.utilities.AssetPool;

public class RenderBatch implements Comparable<RenderBatch> {
    private final int POS_SIZE = 2;
    private final int COL_SIZE = 4;
    private final int TEX_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COL_OFFSET = POS_SIZE * Float.BYTES;
    private final int TEX_OFFSET = COL_OFFSET + COL_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_OFFSET + TEX_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int maxSprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7,8,9};

    private int vaoID, vboID, iboID;
    private int maxBatchSize;
    private Shader shader;
    private int layer;

    private List<Material> materials;

    public RenderBatch(int maxBatchSize, int layer){

        this.layer = layer;
        this.shader = AssetPool.getShader("main");

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.materials = new ArrayList<>();
    }

    public void start(){
        // Generate and bind the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length*Float.BYTES, GL_DYNAMIC_DRAW);

        iboID = glGenBuffers();
        int[] indices = listIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Create the vertex attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COL_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COL_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer sprite){
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;
        
        if(sprite.getMaterial() != null){
            if(!materials.contains(sprite.getMaterial())){
                materials.add(sprite.getMaterial());
            }
        }

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
            System.out.println("Batch is full");
        }
    }

    private void loadVertexProperties(int index){

        SpriteRenderer sprite = this.sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4 colour = sprite.getColour();

        int texID = 0;
        if (sprite.getMaterial() != null){
            for (int i=0; i<materials.size(); i++){
                if (materials.get(i) == sprite.getMaterial()){
                    texID = i +1;
                    break;
                }
            }
            vertices[offset + 8] = texID;
        }

        float xOff = 0f;
        float yOff = 0f;

        for (int i=0; i<4; i++){
            switch (i) {
                case 0 -> {
                    xOff = -0.5f;
                    yOff = 0.5f;
                }
                case 1 -> {
                    xOff = 0.5f;
                    yOff = 0.5f;
                }
                case 2 -> {
                    xOff = 0.5f;
                    yOff = -0.5f;
                }
                case 3 -> {
                    xOff = -0.5f;
                    yOff = -0.5f;
                }
                default -> {
                }
            }

            // Position
            vertices[offset] = sprite.gameObject.transform.position.x + (xOff * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yOff * sprite.gameObject.transform.scale.y);

            // Colour
            vertices[offset + 2] = colour.getX();
            vertices[offset + 3] = colour.getY();
            vertices[offset + 4] = colour.getZ();
            vertices[offset + 5] = colour.getW();

            // Texture coordinates
            vertices[offset + 6] = sprite.getTexCoords()[i].x;
            vertices[offset + 7] = sprite.getTexCoords()[i].y;

            // Texture ID
            vertices[offset + 8] = texID;
            
            offset += VERTEX_SIZE;
        }


    }



    public void render(){
        boolean updateDataNeeded = false;
        for (int i=0; i<numSprites; i++){
            SpriteRenderer sprite = sprites[i];
            if (sprite != null){
                if (sprite.isChanged()){
                    loadVertexProperties(i);
                    sprite.changeApplied();
                    updateDataNeeded = true;
                }
            }
        }
        if (updateDataNeeded){
            
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            
        }
        shader.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        Camera camera = Window.getCurrentScene().getCamera();

        float[] orthoData = camera.getOrthoData();
        Matrix4x4 ortho = Matrix4x4.orthographic(orthoData[0], orthoData[1], orthoData[2], orthoData[3], orthoData[4], orthoData[5]);
        //ortho.logMatrix();
        shader.setUniform("projection", ortho);
        // //window.getProjectionMatrix().logMatrix();
        //shader.setUniform("view", Matrix4x4.view(camera.getPosition(), camera.getRotation()));
        Matrix4x4 viewMatrix = Matrix4x4.view(new Vector4(camera.getPosition().x, camera.getPosition().y, 0, 0), camera.getRotation());
        //viewMatrix.logMatrix();
        shader.setUniform("view", viewMatrix);

        for (int i=0; i<materials.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            glBindTexture(GL_TEXTURE_2D, materials.get(i).getTextureID());
        }
        shader.setUniformArray("uTextures", texSlots);

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindVertexArray(0);

        for (int i=0; i<materials.size(); i++){
            glBindTexture(0, materials.get(i).getTextureID());
        }
        shader.unbind();

    }

    private int[] listIndices(){
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offset = index * 6;
        int val = index * 4;

        //my triangles are defined in a clockwise order

        //triangle 1
        elements[offset + 0] = val + 0;
        elements[offset + 1] = val + 1;
        elements[offset + 2] = val + 2;

        //triangle 2
        elements[offset + 3] = val + 0;
        elements[offset + 4] = val + 2;
        elements[offset + 5] = val + 3;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public boolean hasMaterial(Material tex){
        return this.materials.contains(tex);
    }
        
    public boolean hasMaterialRoom(){
        return this.materials.size() < 8;
    }

    public int getLayer(){
        return this.layer;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.getLayer(), o.getLayer());
    }


}
