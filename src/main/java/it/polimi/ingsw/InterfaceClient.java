package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceClient extends Remote {
    //What the server can call from the client

    String askNickname() throws RemoteException;
    void askParameters() throws RemoteException;
}
