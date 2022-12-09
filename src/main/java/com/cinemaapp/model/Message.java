package com.cinemaapp.model;

public class Message<T> {    
    private String msg;
    private boolean empty;
    private T data;

    public Message(String msg) {
        this.msg = msg;
        this.empty = true;
    }
    
    
    public Message(String msg, T data) {
        this.msg = msg;
        this.empty = false;
        this.data = data;
    }
    
    public String getMsg() {
        return msg;
    }

    public boolean isEmpty() {
        return empty;
    }
    
    public T getData() {
        return data;
    }
    
}
