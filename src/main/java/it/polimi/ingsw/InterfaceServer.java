package it.polimi.ingsw;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    //What the client can call from the server

    void hello (InterfaceClient cl) throws RemoteException;
    boolean sendParameters(int maxPlayerNumber,boolean onlyOneCommonCard) throws RemoteException;
}
