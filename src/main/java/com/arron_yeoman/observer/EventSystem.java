package com.arron_yeoman.observer;

import java.util.ArrayList;
import java.util.List;

import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.observer.events.Event;

public class EventSystem {
    private static List<Observer> observers = new ArrayList<Observer>();

    public static void addObserver(Observer observer){
        observers.add(observer);
    }

    public static void notify(GameObject obj, Event event){
        for(Observer observer : observers){
            observer.onNotify(obj, event);
        }
    }
    
}
