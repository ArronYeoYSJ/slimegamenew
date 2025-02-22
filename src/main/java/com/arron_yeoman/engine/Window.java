package com.arron_yeoman.engine;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.arron_yeoman.engine.audio.AudioManager;
import com.arron_yeoman.engine.io.KBInput;
import com.arron_yeoman.engine.io.KeyBinds;
import com.arron_yeoman.engine.io.MouseInput;
import com.arron_yeoman.eventqueue.EventQueue;
import com.arron_yeoman.maths.Matrix4x4;
import com.arron_yeoman.scenes.Scene;
import com.arron_yeoman.scenes.SceneMaster;
import com.arron_yeoman.scenes.StartScene;


public class Window {

    private static int width, height;
    private String title;
    private static long glfwWindow;
    private static long audioContext;
    private static long audioDevice;

    private static Window window = null;

    private static GLFWWindowSizeCallback windowSizeCallback;
    private Matrix4x4 projectionMatrix;
    private static int minHeight = 300;
    private int minWidth;

    //create floats for background color
    public float bgRed, bgGreen, bgBlue;
    //create floats for camera settings
    private float fov, near, far;
    private static float aspectRatio;

    private static IntBuffer currentHeight = MemoryUtil.memAllocInt(1);
    private static IntBuffer currentWidth = MemoryUtil.memAllocInt(1);
    private static float prevHeight;
    private static float prevWidth;

    public boolean isMouseLocked;
    private static boolean resized;
    public static boolean fullScreen;

    private AudioManager audioManager;


    private float fps;

    private static SceneMaster currentScene;
    private static EventQueue eventQueue;
    private KeyBinds keybinds;

    private Window(int width, int height, String title) {
        Window.width = width;
        this.height = height;
        this.title = title;
        this.aspectRatio = (float) width / height;
        this.isMouseLocked = false;
        this.resized = false;
        this.fullScreen = false;
        this.near = 0.01f;
        this.far = 1000.0f;
        this.fov = 90.0f;
        this.audioManager = new AudioManager();
        this.eventQueue = new EventQueue();
        this.keybinds = new KeyBinds();
    }

    public static Window get(int width, int height, String title) {
        if (Window.window == null) {
            Window.window = new Window(width, height, title);
        }
        return Window.window;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window(1080, 720, "Game");
        }
        return Window.window;
    }

    public void run() {
        init();
        loop();
        destroy();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        createCallbacks();

        glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
            this.width = width;
            this.height = height;
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // centre the created window
        glfwSetWindowPos(glfwWindow, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        glfwSetWindowSizeLimits(glfwWindow, ((int) Math.floor(minHeight * aspectRatio)), minHeight, videoMode.width(), videoMode.height());
        glfwShowWindow(glfwWindow);


        //initialize audio
        String defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = ALC11.alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = ALC11.alcCreateContext(audioDevice, attributes);
        ALC11.alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if(!alCapabilities.OpenAL11) {
            System.err.println("OpenAL 1.1 is not supported");
    
        }else{
            System.out.println("OpenAL: " + ALC11.ALC_MAJOR_VERSION);
        }


        GL.createCapabilities();

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
        //basic anti-aliasing
        GL20.glEnable(GL20.GL_MULTISAMPLE);
        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glCullFace(GL11.GL_BACK);
        GL11.glFrontFace(GL11.GL_CW);

        glfwGetWindowSize(glfwWindow, currentWidth, currentHeight);
        prevHeight = currentHeight.get(0);
        prevWidth = currentWidth.get(0);

        
    }

    private static void createCallbacks() {
        glfwSetCursorPosCallback(glfwWindow, MouseInput::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseInput::mouseButtonCallback);
        glfwSetKeyCallback(glfwWindow, KBInput::keyCallback);
        //enable window resize input handling
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int newWidth, int newHeight) {
                width = newWidth;
                height = newHeight;
                resized = true;
                preserveAspectRatio(window, width, height);
            }
        };
        GLFW.glfwSetWindowSizeCallback(glfwWindow, windowSizeCallback);
    }

    private void loop() {
        float frameStartTime = (float) glfwGetTime();
        float frameEndTime;
        float frameTime = -1.0f;
        
        changeScene(0);
        //glViewport(0, 0, (int)prevWidth, (int)prevHeight);

        while (!glfwWindowShouldClose(glfwWindow)) {
            GL11.glClearColor(0.31f, 0.1f, 0.01f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            //poll for any callbacks
            glfwPollEvents();
            pollForSpecialKeys();
            if (frameTime >= 0) {
                currentScene.update(frameTime);
                this.fps = 1/frameTime;
            }

            glfwSwapBuffers(glfwWindow);

            frameEndTime = (float) glfwGetTime();
            frameTime = frameEndTime - frameStartTime;
            frameStartTime = frameEndTime;

            eventQueue.update();
            audioManager.update();
        }
    }

    public void destroy() {
        unlockMouse();

        ALC11.alcDestroyContext(audioContext);
        ALC11.alcCloseDevice(audioDevice);

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void preserveAspectRatio(long window, int width, int height) {
        //preserve aspect ratio
        System.out.println("Resizing window");
        if (width != prevWidth && height != prevHeight) {
            if (width > (int) Math.floor(height * aspectRatio)) {
                glfwSetWindowSize(window, width, (int) Math.floor(width / aspectRatio));
            } else {
                glfwSetWindowSize(window, (int) Math.floor(height * aspectRatio), height);
            }
        } else if (width != prevWidth && height == prevHeight) {
            glfwSetWindowSize(window, width, (int) Math.floor(width / aspectRatio));
        } else if (width == prevWidth && height != prevHeight) {
            glfwSetWindowSize(window, (int) Math.floor(height * aspectRatio), height);
        }
        glfwGetWindowSize(glfwWindow, currentWidth, currentHeight);
        resizeViewport();
        //GL11.glViewport(0, 0, currentWidth.get(0), currentHeight.get(0));
        prevHeight = currentHeight.get(0);
        prevWidth = currentWidth.get(0);
        resized = false;
    }

    private static void resizeViewport() {
        glViewport(0, 0, (int)prevWidth, (int)prevHeight);
    }

    public static void toggleFullScreen() {
        if (fullScreen) {
            setWindowed();
        } else {
            setFullScreen();
        }
    }

    private static void setFullScreen() {
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowMonitor(glfwWindow, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), 0);
        fullScreen = true;
        resizeViewport();
    }

    private static void setWindowed() {
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowMonitor(glfwWindow, 0, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2, width, height, 0);
        fullScreen = false;
        resizeViewport();
    }

    public void toggleMouseLockState() {
        if (isMouseLocked) {
            unlockMouse();
        } else {
            lockMouse();
        }
    }

    private void lockMouse() {
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        isMouseLocked = true;
    }

    private void unlockMouse() {
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        isMouseLocked = false;
    }

    private static void pollForSpecialKeys() {
        if (KBInput.isKeyPressed(GLFW_KEY_F4)) {
            glfwSetWindowShouldClose(Window.get().glfwWindow, true);
        }
        if (KBInput.keyBeginPress(GLFW_KEY_F11)) {
            toggleFullScreen();
        }
        if (KBInput.keyBeginPress(GLFW_KEY_RIGHT_CONTROL)) {
            Window.get().toggleMouseLockState();
        }
    }

    public void changeScene(int newSceneIndex) {
        switch (newSceneIndex) {
            case 0:
                currentScene = new StartScene();
                break;
            case 1:
                currentScene = new Scene();
                break;
            default:
                break;
        }
    }

    public static EventQueue getEventQueue() {
        return get().eventQueue;
    }

    public static SceneMaster getCurrentScene() {
        return get().currentScene;
    }

    public static int getWidth() {
        return (int)get().prevWidth;
    }

    public static int getHeight() {
        return (int)get().prevHeight;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public float getFPS() {
        return this.fps;
    }


    public static long getGLFWWindow() {
        return get().glfwWindow;
    }
    public AudioManager getAudioManager() {
        return this.audioManager;
    }
}