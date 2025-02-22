package com.arron_yeoman.observer.events;

public class Event {
    public EventEnumerator type;

    public Event(EventEnumerator type){
        this.type = type;
    }

    public Event(){
        this.type = EventEnumerator.NONE;
    }
}
