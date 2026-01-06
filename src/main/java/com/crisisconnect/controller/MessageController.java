package com.crisisconnect.controller;

import com.crisisconnect.model.Message;
import com.crisisconnect.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message dispatchMessage(Message message) {

        return messageService.saveMessage(message);
    }



    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Message>> getChatHistory() {
        List<Message> history = messageService.getAllMessages();
        return ResponseEntity.ok(history);
    }
}