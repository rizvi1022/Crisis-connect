package com.crisisconnect.runner;

import com.crisisconnect.model.Message;
import com.crisisconnect.service.SocketServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

@Component
@Slf4j
public class CrisisConnectRunner implements ApplicationRunner {

    @Autowired
    private SocketServerService socketServerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("═══════════════════════════════════════════════════");
        log.info("🚨 CRISISCONNECT - DISASTER OFFLINE MESSAGING 🚨");
        log.info("═══════════════════════════════════════════════════");

        socketServerService.start();

        log.info("✅ System ready");
        log.info("✍ Type message in console to broadcast");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            Message message = new Message();
            message.setId(UUID.randomUUID().toString());
            message.setSenderName("SERVER");
            message.setContent(input);
            message.setTimestamp(LocalDateTime.now());
            message.setType(Message.MessageType.SYSTEM);

            socketServerService.broadcastMessage(message);

            log.info("📤 Message broadcasted from console: {}", input);
        }
    }
}
