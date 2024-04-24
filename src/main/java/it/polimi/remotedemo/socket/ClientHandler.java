package main.java.it.polimi.remotedemo.socket;

import main.java.it.polimi.remotedemo.controller.AdderController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientHandler implements VirtualView {
    final AdderController controller;
    final Server server;
    final BufferedReader input;
    final VirtualView view;

    public ClientHandler(AdderController controller, Server server, BufferedReader input, BufferedWriter output) {
        this.controller = controller;
        this.server = server;
        this.input = input;
        this.view = new ClientProxy(output);
    }

    public void runVirtualView() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                case "connect" -> {}
                case "add" -> {
                    this.controller.add(Integer.parseInt(input.readLine()));
                    this.server.broadcastUpdate(this.controller.getCurrent());
                }
                case "reset" -> {
                    this.controller.reset();
                    this.server.broadcastUpdate(this.controller.getCurrent());
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    @Override
    public void showValue(Integer number) {
        synchronized (this.view) {
            this.view.showValue(number);
        }
    }

    @Override
    public void reportError(String details) {
        synchronized (this.view) {
            this.view.reportError(details);
        }
    }
}
