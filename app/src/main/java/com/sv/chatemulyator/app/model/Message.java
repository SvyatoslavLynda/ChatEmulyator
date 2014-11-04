package com.sv.chatemulyator.app.model;

/**
 * Created by Created by IntelliJ IDEA.
 * User: SV
 * Date: 04.11.2014
 * Time: 1:27
 * For the ChatEmulyator project.
 */
public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
