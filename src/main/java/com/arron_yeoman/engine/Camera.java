package com.arron_yeoman.engine;

//import com.arronyeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.maths.Vector4;

public class Camera {

    private Vector2 position;
    private Vector4 rotation;
    private float moveSpeed = 1f;
    private float mouseSensitivity = 0.075f;
    private double oldMouseX, oldMouseY, newMouseX, newMouseY = 0;
    private static float[] orthoData = new float[6];

    //3rd person related variables
    //public GameObject target;
    // public float distanceFromTarget = 4;
    // public float angleAroundTarget = 0;
    // public float pitch = 20;
    // public float yaw = 0;
    // public float roll = 0;
    // public float maxPitch = 60;
    // public float minPitch = 0;
    // private boolean lockCameraRotation = true;


    public boolean invertY = false;


    public Camera(Vector2 position) {
        this.position = position;
        this.rotation = new Vector4(0, 0, 0, 0);     
    }

    

    public void update() {

	}


    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector4 getRotation() {
        return this.rotation;
    }

    // public void lockCameraRotation(boolean lock) {
    //     this.lockCameraRotation = lock;
    // }   
    
    public static float[] getOrthoData() {
        float width = Window.getWidth();
        float height = Window.getHeight();
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        orthoData = new float[]{-halfWidth, halfWidth, -halfHeight, halfHeight, 0.0f, 100f};
        return orthoData;
    }
}
