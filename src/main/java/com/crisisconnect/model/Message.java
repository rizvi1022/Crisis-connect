package com.crisisconnect.model;

import jakarta.persistence.*; // Entity annotations-er jonno
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // Ei annotation-ti database table create korbe
@Table(name = "messages")
public class Message {

    @Id // Primary Key
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
        this.timestamp = LocalDateTime.now();
    }

    public enum MessageType {
        TEXT, STATUS_UPDATE, EMERGENCY, LOCATION, SYSTEM
    }

    public enum MessagePriority {
        LOW, NORMAL, HIGH, CRITICAL
    }
}