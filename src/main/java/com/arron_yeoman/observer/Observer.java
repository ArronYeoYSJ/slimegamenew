package com.arron_yeoman.observer;

import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.observer.events.Event;

public interface Observer {
    void onNotify(GameObject gameObject, Event event);
}
