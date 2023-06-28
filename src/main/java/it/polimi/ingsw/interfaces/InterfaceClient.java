package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.NewView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface InterfaceClient extends Remote {
    //What the server can call from the client
    void askParameters() throws RemoteException;
    void askForNewNickname() throws RemoteException;
    void updateView(NewView newView) throws RemoteException;
    void disconnectUser(int whichMessageToShow) throws RemoteException;
    void confirmConnection(boolean bool, int nPlayers, Map<String, Boolean> lobby) throws RemoteException;
    void receiveMessage(String sender, String message, String localDateTime) throws RemoteException;
    void wrongMessageWarning(String message) throws RemoteException;
    void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException;
    void lobbyCreated(boolean typeOfGame, int nPlayers, List<String> lobby) throws RemoteException;
    void waitForLobbyCreation() throws RemoteException;
    void askParametersAgain() throws RemoteException;
    void incorrectMove() throws RemoteException;
    void startClearThread() throws RemoteException;
    void check() throws RemoteException;
    void rejoinedMatch() throws RemoteException;
    void fullLobby() throws RemoteException;
    void notificationForReconnection(String nickname) throws RemoteException;
    void notificationForDisconnection(String nickname) throws RemoteException;

    void notifyConnectedUser(String nickname) throws RemoteException;

    void disconnectedFromLobby(String nickname) throws RemoteException;
}
