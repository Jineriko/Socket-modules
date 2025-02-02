package com.company.project;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Message implements Serializable {
    private String sender;
    private String text;
    private LocalDateTime sentAt;

    public Message(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
