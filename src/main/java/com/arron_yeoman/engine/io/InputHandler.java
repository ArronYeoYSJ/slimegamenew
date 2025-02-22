package com.arron_yeoman.engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class InputHandler {

    public static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    public static boolean[] prevKeys = new boolean[GLFW.GLFW_KEY_LAST];
    public static boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    public static boolean[] prevMouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    public static double mouseX, mouseY;

    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;

    public static void update(){
        for (int i = 0; i < mouseButtons.length; i++) {
            prevMouseButtons[i] = mouseButtons[i];
        }
        for (int i = 0; i < keys.length; i++){
            prevKeys[i] = keys[i];
        }
    }

    public InputHandler() {

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //System.out.println("Key: " + key + " " + action);
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
                else {
                    keys[key] = (action != GLFW.GLFW_RELEASE);
                }
            }
        };

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                //System.out.println("Mouse position: (" + xpos + ", " + ypos + ")");
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                //System.out.println("Mouse button: " + button + " " + action);
                mouseButtons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isKeyJustPressed(int key) {
        if (keys[key]){
            keys[key] = false;
            return true;
        }
        return false;
    }
    public static boolean isMouseButtonJustPressed(int button) {
        // A button is just pressed if it is down now but was not down in the previous frame.
        return mouseButtons[button] && !prevMouseButtons[button];
    }

    public static boolean isMouseButtonDown(int button) {
        return mouseButtons[button];
    }

    public void destroy() {
        keyCallback.free();
        cursorPosCallback.free();
        mouseButtonCallback.free();
    }

    //getters
    public boolean[] getKeys() {
        return keys;
    }

    public boolean[] getMouseButtons() {
        return mouseButtons;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    public GLFWCursorPosCallback getCursorPosCallback() {
        return cursorPosCallback;
    }

    public GLFWMouseButtonCallback getMouseButtonCallback() {
        return mouseButtonCallback;
    }


    // //setters
    // public void setKeys(boolean[] keys) {
    //     this.keys = keys;
    // }
    // public void setMouseButtons(boolean[] mouseButtons) {
    //     this.mouseButtons = mouseButtons;
    // }
    // public void setMouseX(double mouseX) {
    //     this.mouseX = mouseX;
    // }
    // public void setMouseY(double mouseY) {
    //     this.mouseY = mouseY;
    // }
    // public void setKeyCallback(GLFWKeyCallback keyCallback) {
    //     this.keyCallback = keyCallback;
    // }
    // public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
    //     this.cursorPosCallback = cursorPosCallback;
    // }
    // public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
    //     this.mouseButtonCallback = mouseButtonCallback;
    // }

    
}
