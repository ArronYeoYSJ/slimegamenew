package com.arron_yeoman.engine.io;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import com.arron_yeoman.eventqueue.EventQueue;

public class KBInput {
    private static KBInput instance;
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1];
    private boolean keyBeginPress[] = new boolean[GLFW_KEY_LAST + 1];

    private EventQueue eventQueue;

    private KBInput() {
        Arrays.fill(keyPressed, false);
        Arrays.fill(keyBeginPress, false);

    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPress, false);
    }

    public static KBInput get() {
        if (KBInput.instance == null) {
            KBInput.instance = new KBInput();
        }

        return KBInput.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key <= GLFW_KEY_LAST && key >= 0) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
                get().keyBeginPress[key] = true;


            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
                get().keyBeginPress[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode <= GLFW_KEY_LAST && keyCode >= 0) {
            return get().keyPressed[keyCode];
        }

        return false;
    }

    public static boolean keyBeginPress(int keyCode) {
        //System.out.println("Key begin press: " + keyCode);
        if (keyCode <= GLFW_KEY_LAST && keyCode >= 0) {
            boolean b = get().keyBeginPress[keyCode];
            get().keyBeginPress[keyCode] = false;
            //Window.get().getEventQueue().addEvent(new QueueEvent("playAudio", "coin.ogg"));
            return b;
        }
        
        return false;
    }
}
