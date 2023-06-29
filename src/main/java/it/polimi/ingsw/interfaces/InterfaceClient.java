package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.NewView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * The InterfaceClient interface manages the interactions with the client
 */
public interface InterfaceClient extends Remote {
    //What the server can call from the client
    /**
     * Asks the server controller for the game parameters.
     *
     * @throws RemoteException if a remote exception occurs
     */
    void askParameters() throws RemoteException;
    /**
     * Asks the user for a new nickname and send it to the server controller.
     *
     * @throws RemoteException if a remote exception occurs
     */
    void askForNewNickname() throws RemoteException;
    /**
     * Updates the client's view with the new view received from the server.
     *
     * @param newView the new view
     * @throws RemoteException if a remote exception occurs
     */
    void updateView(NewView newView) throws RemoteException;
    /**
     * Print the correct message after the disconnection of a user based on the message input
     *
     * @param whichMessageToShow the message to show
     * @throws RemoteException if a remote exception occurs
     */
    void disconnectUser(int whichMessageToShow) throws RemoteException;
    /**
     * Confirms the connection with the server.
     *
     * @param bool The type of lobby.
     * @param lobby map representing the lobby
     * @param nPlayers number of players
     * @throws RemoteException if a remote exception occurs.
     */
    void confirmConnection(boolean bool, int nPlayers, Map<String, Boolean> lobby) throws RemoteException;
    /**
     * Receives a message from the server.
     *
     * @param sender The sender of the message.
     * @param message The message received.
     * @param localDateTime The timestamp of the message.
     * @throws RemoteException if a remote exception occurs.
     */
    void receiveMessage(String sender, String message, String localDateTime) throws RemoteException;
    /**
     * Notifies the client about receiving a wrong message.
     *
     * @param message The wrong message received.
     * @throws RemoteException if a remote exception occurs.
     */
    void wrongMessageWarning(String message) throws RemoteException;
    /**
     * Receives the cards from the server.
     *
     * @param whichPersonal        The number of the personal target card.
     * @param commonTargetCardList The list of common goal cards.
     * @throws RemoteException if a remote exception occurs.
     */
    void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException;
    /**
     * Notifies the client about the creation of a lobby.
     *
     * @param typeOfGame The type of the game lobby.
     * @throws RemoteException if a remote exception occurs.
     */
    void lobbyCreated(boolean typeOfGame, int nPlayers, Map<String, Boolean> lobby) throws RemoteException;
    /**
     * Waits for the creation of a lobby.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    void waitForLobbyCreation() throws RemoteException;
    /**
     * Asks the client to provide the parameters again.
     */
    void askParametersAgain() throws RemoteException;
    /**
     * Notifies the client about an incorrect move.
     */
    void incorrectMove() throws RemoteException;
    /**
     * Starts a separate thread for clearing the RMI connection.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    void startClearThread() throws RemoteException;
    /**
     * Checks the connection with the server (ping).
     *
     * @throws RemoteException if a remote exception occurs.
     */
    void check() throws RemoteException;
    /**
     * Notifies the client about rejoining a match.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    void rejoinedMatch() throws RemoteException;
    /**
     * Notifies the client about a full lobby.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    void fullLobby() throws RemoteException;
    /**
     * Notifies the client about a player reconnection.
     * @param nickname The nickname of the reconnected player.
     * @throws RemoteException if a remote exception occurs.
     */
    void notificationForReconnection(String nickname) throws RemoteException;
    /**
     * Notifies the client about a player disconnection.
     * @param nickname The nickname of the disconnected player.
     * @throws RemoteException if a remote exception occurs.
     */
    void notificationForDisconnection(String nickname) throws RemoteException;
    /**
     * Notify the server that a new user has connected.
     *
     * @param nickname nickname of the user
     */
    void notifyConnectedUser(String nickname) throws RemoteException;
    /**
     * Notify the server that a user has disconnetted from the lobby.
     *
     * @param nickname nickname of the user
     */
    void disconnectedFromLobby(String nickname) throws RemoteException;
    /**
     * Notify the server that a user has rejoined the lobby.
     *
     * @param lobby Map of the lobby
     * @param numPlayers number of current players
     */
    void rejoinedInLobby(Map<String, Boolean> lobby, int numPlayers) throws RemoteException;
    /**
     * Notify the server that a user has rejoined.
     *
     * @param nickname nickname of the user
     */
    void userRejoined(String nickname) throws RemoteException;
}
