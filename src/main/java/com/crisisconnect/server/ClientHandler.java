package com.crisisconnect.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ServerConnectionManager.addClient(this); //

        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("Received: " + msg);


                ServerConnectionManager.broadcast(msg, this);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            ServerConnectionManager.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }
}
