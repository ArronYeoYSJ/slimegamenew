package com.arron_yeoman.eventqueue;

public class QueueEvent {
    private String type;
    private String data;

    public QueueEvent(String type, String data) {
        this.type = type;
        this.data = data;
    }



    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
