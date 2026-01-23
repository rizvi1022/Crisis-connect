package com.crisisconnect.controller;

import com.crisisconnect.MessageRepository; // Import nishchit koro
import com.crisisconnect.model.StatusEntry;
import com.crisisconnect.service.SocketServerService;
import com.crisisconnect.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Slf4j
public class CrisisConnectController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private SocketServerService socketServerService;

    @Autowired
    private MessageRepository messageRepository; // Database theke count anar jonno

    // Update user status
    @PostMapping("/status")
    public ResponseEntity<StatusEntry> updateStatus(@RequestBody StatusEntry status) {
        statusService.updateStatus(status);
        return ResponseEntity.ok(status);
    }



    // Get network statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Stats gulo database ebong service theke neya hochche
        stats.put("activeConnections", socketServerService.getActiveConnectionCount());
        stats.put("totalMessages", messageRepository.count()); // Ekhane total count asbe
        stats.put("activeUsers", statusService.getActiveUserCount());
        stats.put("criticalUsers", statusService.getCriticalUserCount());
        stats.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(stats);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "CrisisConnect");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }

}
