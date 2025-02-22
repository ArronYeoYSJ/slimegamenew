package com.arron_yeoman.engine.io;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    private KeyActions actions;

    private static Map<KeyActions, Integer> keyBinds;

    public KeyBinds(){
        keyBinds = new HashMap<>();
        setDefaultKeyBinds();
    }

    public static void setDefaultKeyBinds(){
        keyBinds.put(KeyActions.UP, GLFW.GLFW_KEY_W);
        keyBinds.put(KeyActions.DOWN, GLFW.GLFW_KEY_S);
        keyBinds.put(KeyActions.LEFT, GLFW.GLFW_KEY_A);
        keyBinds.put(KeyActions.RIGHT, GLFW.GLFW_KEY_D);
        keyBinds.put(KeyActions.JUMP, GLFW.GLFW_KEY_SPACE);
        keyBinds.put(KeyActions.SHOOT, GLFW.GLFW_KEY_E);
        keyBinds.put(KeyActions.RELOAD, GLFW.GLFW_KEY_R);
        keyBinds.put(KeyActions.PAUSE, GLFW.GLFW_KEY_BACKSPACE);
        keyBinds.put(KeyActions.QUIT, GLFW.GLFW_KEY_ESCAPE);
    }

    public static  void setKeyBind(KeyActions action, int key){
        if (keyBinds.containsKey(action)){
            keyBinds.remove(action);
        }
        keyBinds.put(action, key);
    }

    public static int getKeyBind(KeyActions action){
        return keyBinds.get(action);
    }
}
