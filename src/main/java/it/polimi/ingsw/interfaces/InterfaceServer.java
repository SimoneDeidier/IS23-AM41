package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.server.servercontroller.exceptions.InvalidMoveException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    //What the client can call from the server

    void presentation(InterfaceClient cl, String nickname) throws RemoteException;
    void sendParameters(InterfaceClient cl,int maxPlayerNumber,boolean onlyOneCommonCard) throws RemoteException;
    void executeMove(Body move) throws RemoteException, InvalidMoveException;
    void clearRMI() throws RemoteException;
    void peerToPeerMsgHandler(String sender, String receiver, String text) throws RemoteException;
    void broadcastMsgHandler(String sender, String text) throws RemoteException;
}
