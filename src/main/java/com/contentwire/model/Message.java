package com.contentwire.model;

/**
 * A logical model of E-Mail messages.
 */

public class Message {

    private String subject;
    private String text;

    public Message(String subject, String body) {
        this.subject = subject;
        this.text = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
