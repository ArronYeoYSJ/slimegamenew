package com.arron_yeoman.engine.graphics.shapes;

import com.arron_yeoman.engine.graphics.Material;
import com.arron_yeoman.engine.graphics.Mesh;
import com.arron_yeoman.maths.Vector4;
import com.arron_yeoman.maths.Vertex;


public class Rectangle {

    private Vector4 position;
    private Vector4 color;
    private Vector4 rotation;
    private Vector4 scale;
    private float width;
    private float height;
    private Vertex[] vertices;
    private int[] indices;
    private Mesh mesh;
    private Material material;

    public Rectangle(float width, float height, Vector4 position, Vector4 color, Vector4 rotation, Vector4 scale) {
        
        this.width = width;
        this.height = height;
        this.position = position;
        this.color = color;
        this.rotation = rotation;
        this.scale = scale;
        this.vertices = new Vertex[4];
        this.indices = new int[]{0, 1, 2, 0, 2, 3};
    }

    public Rectangle(float width, float height, Vector4 position, String textureName) {
        
        this.width = width;
        this.height = height;
        this.position = position;
        this.color = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
        this.rotation = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);
        this.scale = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
        this.vertices = new Vertex[4];
        this.indices = new int[]{0, 1, 2, 0, 2, 3};
        this.material = new Material(textureName);
    }

    public void create() {
        float u = 0.0f;
        float v = 1.0f;
        // Create the rectangle
        for (int i = 0; i < 4; i++) {
            if (i%3==0){u = 0.0f;} else {u = 1.0f;}
            if (i < 2){v = 1.0f;} else {v = 0.0f;}
            // Create the vertices
            vertices[i] = new Vertex(new Vector4(-width/2f, height/2f, position.getZ(), 1), color, u, v);
            System.out.println("Vertex: " + i + " x: " + vertices[i].getX() + " y: " + vertices[i].getY() + " z: " + vertices[i].getZ());
            //System.out.println("Vertex: " + i + " r: " + vertices[i].getR() + " g: " + vertices[i].getG() + " b: " + vertices[i].getB() + " a: " + vertices[i].getA());
            System.out.println("Vertex: " + i + " u: " + vertices[i].getU() + " v: " + vertices[i].getV());
            if (i%2==0){width *= -1f;}
            if (i%2==1){height *= -1f;}
            
        }
    }
    
    public void init() {
        // Create the mesh
        mesh = new Mesh(vertices, indices, material);
        mesh.initMesh();
    }

    public Vector4 getPosition() {
        return position;
    } 
    public Vector4 getColor() {
        return color;
    }
    public Vector4 getRotation() {
        return rotation;
    }
    public Vector4 getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }

}
