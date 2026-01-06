package com.crisisconnect.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int PORT = 8888;
    private static final ExecutorService clientThreadPool =
            Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("[SERVER] Starting CrisisConnect Server...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Server running on port " + PORT);

            while (true) {
                System.out.println("[SERVER] Waiting for client...");

                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client connected: " +
                        clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket);
                ServerConnectionManager.addClient(handler); // ✅ FIX
                clientThreadPool.submit(handler);
            }

        } catch (IOException e) {
            System.out.println("[SERVER] Error: " + e.getMessage());
        }
    }
}
