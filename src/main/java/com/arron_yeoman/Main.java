package com.arron_yeoman;


import com.arron_yeoman.engine.Window;


public class Main{

    public static void main(String[] args) {
        Window window = Window.get(1080, 720, "Game");
        window.run();
    }
}