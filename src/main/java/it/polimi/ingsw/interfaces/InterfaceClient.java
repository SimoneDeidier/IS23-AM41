package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.NewPersonalView;
import it.polimi.ingsw.messages.NewView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceClient extends Remote {
    //What the server can call from the client
    void askParameters() throws RemoteException;
    void askForNewNickname() throws RemoteException;
    void updateView(NewView newView) throws RemoteException;
    void updatePersonalView(NewPersonalView newPersonalView) throws RemoteException;
    void disconnectUser(int whichMessageToShow) throws RemoteException;
    void confirmConnection(boolean bool) throws RemoteException;
    void receiveMessage(String message) throws RemoteException;
    void wrongMessageWarning(String message) throws RemoteException;
}