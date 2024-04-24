package main.java.it.polimi.remotedemo.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
    public void showValue(Integer number) throws RemoteException;
    public void reportError(String details) throws RemoteException;
}
