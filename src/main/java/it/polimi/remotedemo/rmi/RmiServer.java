package main.java.it.polimi.remotedemo.rmi;

import main.java.it.polimi.remotedemo.controller.AdderController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServer implements VirtualServer {
    final AdderController controller;
    final List<VirtualView> clients = new ArrayList<>();

    public RmiServer(AdderController controller) {
        this.controller = controller;
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        System.err.println("new client connected");
        // TODO DATA RACE
        this.clients.add(client);
    }

    @Override
    public void add(Integer number) throws RemoteException {
        System.err.println("add request");
        if (this.controller.add(number)) {
            Integer current = this.controller.getCurrent();
            // TODO Non ideale, una soluzione migliore può fare update non bloccante dei clients
            synchronized (this.clients) {
                for (var c : this.clients) {
                    c.showValue(current);
                }
            }
        } else {
            // TODO Non ideale, una soluzione migliore può fare update non bloccante dei clients
            synchronized (this.clients) {
                for (var c : this.clients) {
                    c.reportError("failed to add");
                }
            }
        }
    }

    @Override
    public void reset() throws RemoteException {
        System.err.println("reset request");
        if (this.controller.reset()) {
            Integer current = this.controller.getCurrent();
            // TODO Non ideale, una soluzione migliore può fare update non bloccante dei clients
            synchronized (this.clients) {
                for (var c : this.clients) {
                    c.showValue(current);
                }
            }
        } else {
            // TODO Non ideale, una soluzione migliore può fare update non bloccante dei clients
            synchronized (this.clients) {
                for (var c : this.clients) {
                    c.reportError("failed to reset");
                }
            }
        }
    }

    public static void main(String[] args) throws RemoteException {
        String name = "VirtualServer";

        VirtualServer engine = new RmiServer(new AdderController());
        VirtualServer stub =
                (VirtualServer) UnicastRemoteObject.exportObject(engine, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(name, stub);
        System.out.println("Adder bound");
    }
}
