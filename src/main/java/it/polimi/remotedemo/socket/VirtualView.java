package main.java.it.polimi.remotedemo.socket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView {
    public void showValue(Integer number);
    public void reportError(String details);
}
