package com.arron_yeoman.eventqueue;

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
    private static Queue<QueueEvent> queue;
    private static EventHandler eventHandler; 

    public EventQueue() {
        eventHandler = new EventHandler(this);
        init();
    }

    private static void init() {
        System.out.println("Initializing event queue");
        queue = new LinkedList<>();
    }

    public void update() {
        eventHandler.handleEvents();
    }

    public static void addEvent(QueueEvent event) {
        System.out.println("Adding event: " + event.getType());
        queue.add(event);
    }

    public QueueEvent getEvent() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
