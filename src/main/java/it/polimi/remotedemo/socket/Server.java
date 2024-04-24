package main.java.it.polimi.remotedemo.socket;

import main.java.it.polimi.remotedemo.controller.AdderController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    final ServerSocket listenSocket;
    final AdderController controller;
    final List<ClientHandler> clients = new ArrayList<>();

    public Server(ServerSocket listenSocket, AdderController controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this.controller, this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

            clients.add(handler);
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void broadcastUpdate(Integer value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showValue(value);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = Integer.parseInt("1234");

        ServerSocket listenSocket = new ServerSocket(port);

        new Server(listenSocket, new AdderController());
    }
}