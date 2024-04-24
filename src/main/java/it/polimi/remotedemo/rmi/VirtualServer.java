package main.java.it.polimi.remotedemo.rmi;

import main.java.it.polimi.remotedemo.model.AdderModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    public void add(Integer number) throws RemoteException;

    public void reset() throws RemoteException;
}
