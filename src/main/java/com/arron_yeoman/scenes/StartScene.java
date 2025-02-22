package com.arron_yeoman.scenes;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.arron_yeoman.engine.Camera;
import com.arron_yeoman.engine.Transform;
import com.arron_yeoman.engine.Window;
import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.engine.gameobjects.components.Sprite;
import com.arron_yeoman.engine.gameobjects.components.SpriteRenderer;
import com.arron_yeoman.engine.graphics.BatchRenderer;
import com.arron_yeoman.engine.io.KBInput;
import com.arron_yeoman.engine.io.KeyActions;
import com.arron_yeoman.engine.io.KeyBinds;
import com.arron_yeoman.maths.Vector2;
import com.arron_yeoman.utilities.AssetPool;

public class StartScene extends SceneMaster {

    private GameObject title;
    private GameObject startButton;
    private GameObject optionsButton;
    private GameObject quitButton;
    private boolean startGame = false;
    private List<GameObject> gameObjects = new ArrayList<>();
    protected Map<String, GameObject> gameObjectMap = new HashMap<>();

    private BatchRenderer batchRenderer;
    private Camera camera = new Camera(new Vector2(0, 0));


    private boolean isFirstRun = true;
    private boolean changingScene = false;
    private boolean inOptions = false;

    private static String keyToChange = "";
    private static String keyToChangeTo = "";
    private static boolean changingKey = false;

    public int optionToHighlight = 0;
    public int keyToHighlight = 0;

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void init() {
        loadResources();
        this.batchRenderer = new BatchRenderer();
        createGOs();
              

    }

    private void createGOs(){
        // Create title
        GameObject title = new GameObject("title", new Transform(new Vector2(0, 0), new Vector2(1080, 720)), 0);
        title.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("background.jpg"))));
        this.addGO(title);

        GameObject backDrop = new GameObject("backDrop", new Transform(new Vector2(0, 0), new Vector2(600, 666)), 0);
        backDrop.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("backDrop.png"))));
        this.addGO(backDrop);

        GameObject keyNames = new GameObject("keyNames", new Transform(new Vector2(-50, 1000), new Vector2(350, 555)), 1);
        keyNames.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("keys.png"))));
        this.addGO(keyNames);


        startButton = new GameObject("startButton", new Transform(new Vector2(00, 200), new Vector2(300, 150)), 1);
        startButton.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("play.png"))));
        this.addGO(startButton);

        optionsButton = new GameObject("optionsButton", new Transform(new Vector2(00, 0), new Vector2(300, 150)), 1);
        optionsButton.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("options.png"))));
        this.addGO(optionsButton);

        quitButton = new GameObject("quitButton", new Transform(new Vector2(00, -200), new Vector2(300,150)), 1);
        quitButton.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("quit.png"))));
        this.addGO(quitButton);

        genKeys();

        
    }

    public void genKeys(){
        char[] keys = "abcedfghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < 26; i++){
            GameObject go = new GameObject("key_" + keys[i], new Transform(new Vector2(150, 1000), new Vector2(100, 100)), 1);
            go.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("key_" + keys[i] + ".png"))));
            this.addGO(go);
        }
        for (int i = 0; i < 10; i++){
            GameObject go = new GameObject("key_" + i, new Transform(new Vector2(150, 1000), new Vector2(100, 100)), 1);
            go.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("key_" + i + ".png"))));
            this.addGO(go);
        }
        GameObject go = new GameObject("key_space", new Transform(new Vector2(150, 1000), new Vector2(130, 100)), 1);
        go.addComponent(new SpriteRenderer(new Sprite(AssetPool.getMaterial("key_space.png"))));
        this.addGO(go);


    }

    @Override
    public void update(float dt) {

        if(isFirstRun){
            init();
            isFirstRun = false;
        }
        if(changingScene){
            return;
        }
        this.batchRenderer.render();
        camera.update();

        //System.out.println(inOptions);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        if(startGame){
            changingScene = true;
            Window.get().changeScene(1);
            return;

        }
        if (changingKey){
            for (int key = 32; key < 348; key++) {
                if (KBInput.keyBeginPress(key)) {
                    keyToChangeTo = KeyEvent.getKeyText(key);
                    keyToChangeTo = keyToChangeTo.toLowerCase();
                    changingKey = false;
                    changingKey = false;
                    System.out.println("captured key: " + keyToChangeTo);
                    rebindKey(key);
                    break;
                }
            }
        }

        if (inOptions && KBInput.keyBeginPress(GLFW.GLFW_KEY_BACKSPACE)){
            hideOptions();
            showMenuOptions();
            inOptions = false;
        }
        
            
        if (!inOptions && KBInput.keyBeginPress(GLFW.GLFW_KEY_ENTER)) {
            switch (optionToHighlight) {
                case 0:
                    startGame = true;
                    break;
                case 1:
                    System.out.println("Options");
                    hideMenuOptions();
                    showOptions();
                    inOptions = true;
                    break;
                case 2:
                    System.out.println("Quit");
                    GLFW.glfwSetWindowShouldClose(Window.get().getGLFWWindow(), true);
                    break;
                default:
                    break;
            }
        }else if (KBInput.keyBeginPress(GLFW.GLFW_KEY_ENTER)) {
            System.out.println("Enter pressed in options");
            changingKey = true;
            switch (keyToHighlight) {
                case 0:
                    keyToChange = "jump";
                    break;
                case 1:
                    keyToChange = "s";
                    break;
                case 2:
                    keyToChange = "a";
                    break;
                case 3:
                    keyToChange = "d";
                    break;
                default:
                    break;
            }
        }
        if (KBInput.keyBeginPress(GLFW.GLFW_KEY_DOWN) && !changingKey) {
            if (!inOptions){
                optionToHighlight++;
                if (optionToHighlight > 2) {
                    optionToHighlight = 0;
                }
            } else {
                keyToHighlight++;
                if (keyToHighlight > 3){
                    keyToHighlight = 0;
                }
            }
        }
        if (KBInput.keyBeginPress(GLFW.GLFW_KEY_UP) && !changingKey) {
            if (!inOptions){
                optionToHighlight--;
                if (optionToHighlight < 0) {
                    optionToHighlight = 2;
                }
            } else {
                keyToHighlight--;
                if (keyToHighlight < 0){
                    keyToHighlight = 3;
                }
            }
        }
        if(!inOptions){
            highlightMenuOption(optionToHighlight);
        } else {
            highlightMenuOption(keyToHighlight);
        }
        
    }

    private void rebindKey(int key) {
        System.out.println("Rebinding key");
        switch (keyToChange) {
            case "jump":
                System.out.println("Rebinding jump to " + keyToChangeTo);
                KeyBinds.setKeyBind(KeyActions.JUMP, key);
                gameObjectMap.get("key_space").transform.setTransform(new Vector2(1000, 200));
                gameObjectMap.get("key_" + keyToChangeTo).transform.setTransform(new Vector2(180, 200));
                break;
            case "s":
                System.out.println("Rebinding s to " + keyToChangeTo);
                KeyBinds.setKeyBind(KeyActions.DOWN, key);
                gameObjectMap.get("key_s").transform.setTransform(new Vector2(1000, 80));
                gameObjectMap.get("key_" + keyToChangeTo).transform.setTransform(new Vector2(180, 80));
                break;
            case "a":
                System.out.println("Rebinding a to " + keyToChangeTo);
                KeyBinds.setKeyBind(KeyActions.LEFT, key);
                gameObjectMap.get("key_a").transform.setTransform(new Vector2(1000, -40));
                gameObjectMap.get("key_" + keyToChangeTo).transform.setTransform(new Vector2(180, -40));
                break;
            case "d":
                System.out.println("Rebinding d to " + keyToChangeTo);
                KeyBinds.setKeyBind(KeyActions.RIGHT, key);
                gameObjectMap.get("key_d").transform.setTransform(new Vector2(1000, -180));
                gameObjectMap.get("key_" + keyToChangeTo).transform.setTransform(new Vector2(180, -180));
                break;
            default:
                break;
        }

    }

    private void hideMenuOptions() {
        System.out.println("Hiding menu options");
        startButton.transform.setTransform(new Vector2(1000,startButton.transform.position.y));
        optionsButton.transform.setTransform(new Vector2(1000,optionsButton.transform.position.y));
        quitButton.transform.setTransform(new Vector2(1000,quitButton.transform.position.y));
    }

    private void showMenuOptions() {
        startButton.transform.setTransform(new Vector2(0,startButton.transform.position.y));
        optionsButton.transform.setTransform(new Vector2(0,optionsButton.transform.position.y));
        quitButton.transform.setTransform(new Vector2(0,quitButton.transform.position.y));
    }

    private void showOptions(){
        gameObjectMap.get("keyNames").transform.setTransform(new Vector2(-55, 0));
        gameObjectMap.get("key_space").transform.setTransform(new Vector2(180, 200));
        gameObjectMap.get("key_s").transform.setTransform(new Vector2(180, 80));
        gameObjectMap.get("key_a").transform.setTransform(new Vector2(180, -40));
        gameObjectMap.get("key_d").transform.setTransform(new Vector2(180, -180));

    }
    private void hideOptions(){
        gameObjectMap.get("keyNames").transform.setTransform(new Vector2(-55, 1000));
        gameObjectMap.get("key_space").transform.setTransform(new Vector2(1000, 200));
        gameObjectMap.get("key_s").transform.setTransform(new Vector2(1000, 80));
        gameObjectMap.get("key_a").transform.setTransform(new Vector2(1000, -40));
        gameObjectMap.get("key_d").transform.setTransform(new Vector2(1000, -180));
    }

    private void highlightMenuOption(int option) {
        if (!inOptions){
            switch (option) {
                case 0:
                    startButton.transform.scale = new Vector2(350, 200);
                    optionsButton.transform.scale = new Vector2(300, 150);
                    quitButton.transform.scale = new Vector2(300, 150);
                    break;
                case 1:
                    optionsButton.transform.scale = new Vector2(350, 200);
                    startButton.transform.scale = new Vector2(300, 150);
                    quitButton.transform.scale = new Vector2(300, 150);
                    break;
                case 2:
                    quitButton.transform.scale = new Vector2(350, 200);
                    startButton.transform.scale = new Vector2(300, 150);
                    optionsButton.transform.scale = new Vector2(300, 150);
                    break;
                default:
                    break;
            }
        } else {
            switch (option) {
                case 0:
                    gameObjectMap.get("key_space").transform.scale = new Vector2(150, 120);
                    gameObjectMap.get("key_s").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_a").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_d").transform.scale = new Vector2(100, 100);
                    break;
                case 1:
                    gameObjectMap.get("key_s").transform.scale = new Vector2(130, 100);
                    gameObjectMap.get("key_space").transform.scale = new Vector2(120, 120);
                    gameObjectMap.get("key_a").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_d").transform.scale = new Vector2(100, 100);
                    break;
                case 2:
                    gameObjectMap.get("key_a").transform.scale = new Vector2(130, 100);
                    gameObjectMap.get("key_s").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_space").transform.scale = new Vector2(120, 120);
                    gameObjectMap.get("key_d").transform.scale = new Vector2(100, 100);
                    break;
                case 3:
                    gameObjectMap.get("key_d").transform.scale = new Vector2(130, 100);
                    gameObjectMap.get("key_s").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_a").transform.scale = new Vector2(100, 100);
                    gameObjectMap.get("key_space").transform.scale = new Vector2(120, 120);
                    break;
                default:
                    break;
            }
        }
    }
    public void setGameOver(){};

    private void loadResources() {
        AssetPool.getMaterial("background.jpg");
        AssetPool.getMaterial("play.png");
        AssetPool.getMaterial("options.png");
        AssetPool.getMaterial("quit.png");
        AssetPool.getMaterial("backDrop.png");
        
    }

    public void addGO(GameObject go){
        //System.out.println("Adding game objects to scene");
        gameObjects.add(go);
        gameObjectMap.put(go.getName(), go);
        //physics.addRigidBody(go.getComponent(RigidBody2D.class), go.isDynamic);

        go.start();
        this.batchRenderer.add(go);
    }

    public static void receiveKeyPress(String key){
        // System.out.println("Received key press: " + key);
        // if (changingKey){

        //     int code = Integer.parseInt(key);
        //     keyToChangeTo = KeyEvent.getKeyText(code);
        //     System.out.println("Changing " + keyToChange + " to " + keyToChangeTo);
        // }
    }
}