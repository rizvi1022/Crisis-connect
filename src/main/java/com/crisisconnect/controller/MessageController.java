package com.crisisconnect.controller;

import com.crisisconnect.MessageRepository;
import com.crisisconnect.model.Message;
import com.crisisconnect.model.StatusEntry;
import com.crisisconnect.service.MessageService;
import com.crisisconnect.service.SocketServerService;
import com.crisisconnect.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatusService statusService; // Status handle করার জন্য

    @Autowired
    private SocketServerService socketServerService; // কাউন্টের জন্য

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // ব্রডকাস্টের জন্য

    // ১. মেসেজ পাঠানো এবং অটো-ফিল্টার
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public Message dispatchMessage(@Payload Message message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

       
        if (message.getContent() != null && message.getContent().toLowerCase().contains("badword")) {
            message.setContent("[Auto-Filtered] This content was removed.");
        }

        if ("EMERGENCY".equals(message.getType())) {
            message.setPriority(Message.MessagePriority.CRITICAL);
        } else if (message.getPriority() == null) {
            message.setPriority(Message.MessagePriority.NORMAL);
        }

        return messageService.saveMessage(message);
    }

   
    @MessageMapping("/status.update")
    public void handleStatusUpdate(@Payload StatusEntry status) {
        try {
            
            statusService.updateStatus(status);

            
            messagingTemplate.convertAndSend("/topic/status", status);

            log.info("Broadcasted Status for: {}", status.getUserName());
        } catch (Exception e) {
            log.error("Update error: {}", e.getMessage());
        }
    }

   
    @MessageMapping("/chat.addUser")
    public void addUser() {
        int count = socketServerService.getActiveConnectionCount();
        messagingTemplate.convertAndSend("/topic/userCount", count);
    }

    
    @MessageMapping("/chat.deleteMessage")
    @SendTo("/topic/messages")
    public Map<String, Object> deleteMessage(@Payload Map<String, Object> payload) {
        try {
            Object idObj = payload.get("id");
            if (idObj == null) return null;

            Long id = Long.valueOf(idObj.toString());
            messageRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("deleteId", id);
            response.put("action", "DELETE");
            return response;
        } catch (Exception e) {
            log.error("Error in delete: {}", e.getMessage());
            return null;
        }
    }

    
    @MessageMapping("/chat.editMessage")
    @SendTo("/topic/messages")
    public Map<String, Object> editMessage(@Payload Map<String, Object> payload) {
        Long id = Long.valueOf(payload.get("id").toString());
        String newContent = payload.get("content").toString();

        try {
            Message msg = messageRepository.findById(id).orElse(null);
            if(msg != null) {
                msg.setContent(newContent);
                messageRepository.save(msg);
            }
        } catch (Exception e) {
            log.error("Error editing message: {}", e.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("content", newContent);
        response.put("action", "EDIT");
        return response;
    }

    // --- HTTP API endpoints ---

    @GetMapping("/api/messages/recent")
    public ResponseEntity<List<Message>> getRecentMessages() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return ResponseEntity.ok(messageRepository.findByTimestampAfterOrderByTimestampAsc(oneHourAgo));
    }

    @GetMapping("/api/status")
    public ResponseEntity<List<StatusEntry>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }
}
