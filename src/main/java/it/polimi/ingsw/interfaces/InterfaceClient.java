package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceClient extends Remote {
    //What the server can call from the client
    void askParameters() throws RemoteException;
    void askForNewNickname() throws RemoteException;
    void updateView(List<Player> playersList) throws RemoteException;
    void disconnectUser(int whichMessageToShow) throws RemoteException;
    void confirmConnection(boolean bool) throws RemoteException;
    void receiveMessage(String sender, String message) throws RemoteException;
    void wrongMessageWarning(String message) throws RemoteException;
    void receivePersonalTargetCard(PersonalTargetCard personalTargetCard) throws RemoteException;
}
