package main.java.it.polimi.remotedemo.socket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer {
    public void connect(VirtualView client);

    public void add(Integer number);

    public void reset();
}
