package com.crisisconnect.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerConnectionManager {

    private static final Set<ClientHandler> clients =
            Collections.synchronizedSet(new HashSet<>());

    public static void addClient(ClientHandler client) {
        clients.add(client);
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {

                client.sendMessage(message);
            }
        }
    }
}
