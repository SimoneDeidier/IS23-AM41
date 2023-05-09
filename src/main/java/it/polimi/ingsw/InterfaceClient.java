package it.polimi.ingsw;

import it.polimi.ingsw.server.model.BoardFactory;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.servercontroller.NewPersonalView;
import it.polimi.ingsw.server.servercontroller.NewView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceClient extends Remote {
    //What the server can call from the client

    String askNickname() throws RemoteException;
    void askParameters() throws RemoteException;
    void updateView(NewView newView) throws RemoteException;
    void updatePersonalView(NewPersonalView newPersonalView) throws RemoteException;
    void disconnectUser() throws RemoteException;
}
