package it.polimi.ingsw;

import it.polimi.ingsw.server.servercontroller.Body;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    //What the client can call from the server

    void hello (InterfaceClient cl,String nickname) throws RemoteException;
    boolean sendParameters(int maxPlayerNumber,boolean onlyOneCommonCard) throws RemoteException;
    boolean executeMove(Body move) throws RemoteException;
    void sendMessage(InterfaceClient cl,String message) throws RemoteException;
}
