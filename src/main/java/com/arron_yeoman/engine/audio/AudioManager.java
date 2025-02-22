package com.arron_yeoman.engine.audio;

import com.arron_yeoman.utilities.AssetPool;

public class AudioManager {
    private int pendingAudio = 0;
    private int maxAudio = 16;
    private Sound[] audio = new Sound[maxAudio];
    private int nextSound = 0;
    private int lastSound = 0;

    public AudioManager() {
    }

    public void update() {
        playSound();
    }

    public void queueSound(String soundName) {
        System.out.println("Queueing sound: " + soundName);
        Sound sound = AssetPool.getSound(soundName);
        addSound(sound);
    }

    private void addSound(Sound sound) {
        
        if (pendingAudio < maxAudio) {
            System.out.println("Adding sound");
            audio[nextSound] = sound;
            lastSound = (lastSound + 1) % maxAudio;
            pendingAudio++;
        }
        else {
            assert false : "No more audio slots available";
        }
            
    }

    private void playSound(){
        if (pendingAudio > 0) {
            System.out.println("Playing sound");
            audio[nextSound].play();
            nextSound = (nextSound + 1) % maxAudio;
            pendingAudio--;
        }
    }
}
