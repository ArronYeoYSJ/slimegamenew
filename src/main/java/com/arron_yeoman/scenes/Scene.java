package com.arron_yeoman.scenes;

import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arron_yeoman.engine.gameobjects.*;
import com.arron_yeoman.engine.gameobjects.components.*;
import com.arron_yeoman.engine.graphics.*;
import com.arron_yeoman.maths.*;

import org.lwjgl.glfw.GLFW;

import com.arron_yeoman.engine.io.KBInput;
import  com.arron_yeoman.engine.graphics.shapes.Rectangle;
import com.arron_yeoman.physics2d.Physics2D;
import com.arron_yeoman.physics2d.rigidBody2d.RigidBody2D;
import com.arron_yeoman.utilities.AssetPool;

import com.arron_yeoman.engine.Camera;
import  com.arron_yeoman.engine.Transform;
import com.arron_yeoman.engine.Window;
import com.arron_yeoman.observer.EventSystem;
import com.arron_yeoman.observer.events.Event;
import com.arron_yeoman.observer.events.EventEnumerator;
import com.arron_yeoman.engine.audio.Sound;




public class Scene extends SceneMaster {

    protected List<GameObject> gameObjects = new ArrayList<GameObject>();
    protected Map<String, GameObject> gameObjectMap = new HashMap<>();

    private boolean isRunning = false;
    private boolean isFirstRun = true;
    private boolean changingScene = false;
    private boolean userInput = true;
    private boolean paused = false;
    private float fadeOutTime = 1f;
    private float timeElapsed = 0f;
    private Shader shader;
    private Rectangle rect;
    protected Camera camera;
    protected BatchRenderer batchRenderer;


    private float time = 0.0f;
    private float spriteSpeed = 12f;
    private float spriteSwapTime;
    private int spriteIndex = 0;

 

    public Physics2D physics = new Physics2D((1f/60f), new Vector2(0, -200f));
    Transform objectOneTrans, objectTwoTrans, brickTrans, brickTrans2, brickTrans3;
    RigidBody2D objectOneRB, objectTwoRB, brickRB, brickRB2, brickRB3;


    public Scene() {
        init();
        start();
    }

    

    @Override
    public void init(){

        List<float[]> terrainData = new ArrayList<>();
        // x, y, width, height, layer
        terrainData.add(new float[]{-500,-350,50,50,1});
        terrainData.add(new float[]{-450,-350,50,50,1});
        terrainData.add(new float[]{-400,-350,50,50,1});


        
        loadResources();
        SpriteSheet marioSheet = AssetPool.getSpriteSheet("marioSheet.png");
        SpriteSheet marioBlocks = AssetPool.getSpriteSheet("mario_blocks.png");
        spriteSwapTime = 1f / spriteSpeed;
    
        GameObject go1 = GameObject.createGameObject("Player", new Vector2(-350, -290), new Vector2(60, 60), marioSheet.getSprite(8), "pillcollider", 1, 10.0f, 0.0f, true);
        go1.addComponent(new PlayerController());
        this.addGO(go1);

        GameObject go2 = GameObject.createGameObject("Goomba", new Vector2(200, -290), new Vector2(40, 60), marioSheet.getSprite(18), "AABB", 1, 10.0f, 0.0f, true);
        go2.addComponent(new goombaController());
        this.addGO(go2);

        // GameObject gameover = new GameObject("gameover", new Transform(new Vector2(1000, 0), new Vector2(1080, 720)), 1);
        // gameover.addComponent(new SpriteRenderer( new Sprite(AssetPool.getMaterial("gameover.jpg"))));
        // this.addGO(gameover);

        //genTerrainBlocks(terrainData, marioBlocks);

        genLineOfBlocks(20, new Vector2(-520,-350), new Vector2(50,50), true, marioBlocks.getSprite(1), 1);
        genLineOfBlocks(25, new Vector2(-520,-350), new Vector2(50,50), false, marioBlocks.getSprite(1), 1);
        genLineOfBlocks(25, new Vector2(520,-350), new Vector2(50,50), true, marioBlocks.getSprite(1), 1);
        
        this.batchRenderer = new BatchRenderer();
        this.camera = new Camera(new Vector2(0,0));



    };

    public void genLineOfBlocks(int numBlocks, Vector2 start, Vector2 size,boolean vertical, Sprite sprite, int layer){
        if(!vertical){
            for (int i = 0; i < numBlocks; i++){
                this.addGO(GameObject.createGameObject("brick" + i, new Vector2(start.x + (i * size.x), start.y), size, sprite, "AABB", layer, 0f, 0f, false));
            }
        }
        else{
            for (int i = 0; i < numBlocks; i++){
                this.addGO(GameObject.createGameObject("brick" + i, new Vector2(start.x, start.y + (i * size.y)), size, sprite, "AABB", layer, 0f, 0f, false));
            }
        }

    }


    public void genTerrainBlocks(List<float[]> terrainData, SpriteSheet ss){
        int i = 0;
        for (float[] t : terrainData) {
            i++;
            this.addGO(GameObject.createGameObject("brick" + i, new Vector2(t[0], t[1]), new Vector2(t[2], t[3]), ss.getSprite(1), "AABB", (int)t[4], 0f, 0f, false));
            
        }

    }
    public void loadResources(){
        AssetPool.getShader("main").create();
        AssetPool.getMaterial("beautiful.png");
        AssetPool.getMaterial("checkerboard.png");
        AssetPool.addSpriteSheet("marioSheet.png", new SpriteSheet(new Material("marioSheet.png"),26, 16, 16, 0));
        AssetPool.addSpriteSheet("mario_blocks.png", new SpriteSheet(new Material("mario_blocks.png"), 96, 8, 12, 0));
        AssetPool.addSound("mario_theme", new Sound("mario_theme.ogg", true));
        AssetPool.addSound("coin.ogg", new Sound("coin.ogg", false));
 
    }

    public void start(){
        //System.out.println("Starting scene");
        for (GameObject go : gameObjects) {
            go.start();
            this.batchRenderer.add(go);
        }
        isRunning = true;
        System.out.println("Scene started");
        
    }

    public void addGO(GameObject go){
        //System.out.println("Adding game objects to scene");
        gameObjects.add(go);
        gameObjectMap.put(go.getName(), go);
        physics.addRigidBody(go.getComponent(RigidBody2D.class), go.isDynamic);

        if(isRunning){
            go.start();
            this.batchRenderer.add(go);
        }
    }

    @Override
    public void update(float dt){
        //System.out.println("Updating scene");
        // renderer.renderMesh(rect, camera);

        this.batchRenderer.render();
        physics.update(dt);

        //.transform.setTransform(new Vector2(testObject.getComponent(RigidBody2D.class).getPosition()));
        //testObject2.transform.setTransform(new Vector2(testObject2.getComponent(RigidBody2D.class).getPosition()));

        if(!paused){
            time += dt;
            AssetPool.getSound("mario_theme").play();
        }
        if (time > spriteSwapTime){
            time = 0.0f;
            spriteIndex++;
            if (spriteIndex >= 4){
                spriteIndex = 0;
            }
            gameObjectMap.get("Player").getComponent(SpriteRenderer.class).setSprite(AssetPool.getSpriteSheet("marioSheet.png").getSprite(spriteIndex));
            //testObject.getComponent(RigidBody2D.class).getPosition().printString();
            //Window.get().getEventQueue().addEvent(new QueueEvent("playAudio", "coin.ogg"));
            //AssetPool.getSound("coin.ogg").play();
        }

        camera.update();
        
        //pauses scene
        if (KBInput.keyBeginPress(GLFW.GLFW_KEY_ENTER)){
            if (paused){
                System.out.println("Resuming");
                EventSystem.notify(null, new Event(EventEnumerator.START_PLAY));
                paused = false;
            } else {
                System.out.println("Pausing");
                EventSystem.notify(null, new Event(EventEnumerator.END_PLAY));
                paused = true;
            }
        }

        for (GameObject go : this.gameObjects) {
            System.out.println("Updating game object " + go.getName());
            go.update(dt);
        }
        //printFPS(dt);
    }

    public void printFPS(float dt){timeElapsed += dt;
        if (timeElapsed > 1f){
            timeElapsed = 0f;
            System.out.println("FPS: " + Window.get().getFPS());
        }  
    }

    public Camera getCamera(){
        return this.camera;
    }
    

}
