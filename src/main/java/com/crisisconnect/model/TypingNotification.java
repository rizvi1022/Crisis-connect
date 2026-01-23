package com.crisisconnect.model;

public class TypingNotification {
    private String senderName;
    private boolean typing;

    // Default Constructor (JSON theke object bananor jonno dorkari)
    public TypingNotification() {}

    // Argument Constructor
    public TypingNotification(String senderName, boolean typing) {
        this.senderName = senderName;
        this.typing = typing;
    }

    // Getters and Setters
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}