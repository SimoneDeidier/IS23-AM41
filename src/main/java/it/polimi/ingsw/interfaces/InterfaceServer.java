package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.Body;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    //What the client can call from the server

    void presentation(InterfaceClient cl, String nickname) throws RemoteException;
    boolean sendParameters(int maxPlayerNumber,boolean onlyOneCommonCard) throws RemoteException;
    boolean executeMove(Body move) throws RemoteException;
    // void sendMessage(InterfaceClient cl,String message) throws RemoteException;

    void updateViewRMI() throws RemoteException;
}
