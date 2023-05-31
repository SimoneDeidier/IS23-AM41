package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.NewView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceClient extends Remote {
    //What the server can call from the client
    void askParameters() throws RemoteException;
    void askForNewNickname() throws RemoteException;
    void updateView(NewView newView) throws RemoteException;
    void disconnectUser(int whichMessageToShow) throws RemoteException;
    void confirmConnection(boolean bool) throws RemoteException;
    void receiveMessage(String sender, String message, String localDateTime) throws RemoteException;
    void wrongMessageWarning(String message) throws RemoteException;
    void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException;
    void lobbyCreated(boolean typeOfGame) throws RemoteException;
    void waitForLobbyCreation() throws RemoteException;
    void askParametersAgain() throws RemoteException;
    void incorrectMove() throws RemoteException;
    void startClearThread() throws RemoteException;
    void check() throws RemoteException;
}
