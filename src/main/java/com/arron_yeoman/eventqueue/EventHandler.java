package com.arron_yeoman.eventqueue;

import com.arron_yeoman.engine.Window;
import com.arron_yeoman.scenes.StartScene;

public class EventHandler {
    private static EventQueue eventQueue;

    public EventHandler(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public static void handleEvents() {
        //System.out.println("Handling events");
        while (!eventQueue.isEmpty()) {
            //System.out.println("Handling event");
            QueueEvent event = eventQueue.getEvent();
            String type = event.getType();
            String data = event.getData();

            if (type.equals("playAudio")){
                playAudio(data);
            }

            if (type.equals("keyBeginPress")) {
                System.out.println("Key begin press: " + data);
                StartScene.receiveKeyPress(data);
            }
        }
    }

    private static void playAudio(String data) {
        //System.out.println("Playing audio: " + data);
        Window.get().getAudioManager().queueSound(data);
    }
 

}
