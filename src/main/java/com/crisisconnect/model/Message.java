package com.crisisconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String senderId;
    private String senderName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessagePriority priority;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // --- ENUM DEFINITIONS ---
    public enum MessageType {
        TEXT, STATUS_UPDATE, EMERGENCY, LOCATION, SYSTEM
    }

    public enum MessagePriority {
        LOW, NORMAL, HIGH, CRITICAL
    }

    // --- FIX: Getter and Setter ---
    
    public String getContent() {
        return this.content; 
    }

    public void setContent(String content) {
        this.content = content; 
    }
}
