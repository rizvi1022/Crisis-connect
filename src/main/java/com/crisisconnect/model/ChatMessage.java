package com.crisisconnect.model;

public class ChatMessage {
    private String senderId;
    private String senderName;
    private String content;
    private String type;
    private String priority;
    private String timestamp;

    // Default Constructor
    public ChatMessage() {}

    // Getters and Setters (এগুলো অবশ্যই থাকতে হবে)
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
