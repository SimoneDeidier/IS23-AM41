package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.Body;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    //What the client can call from the server
    /**
     * Receives the presentation from the client and handles the connection process.
     *
     * @param cl The client interface (CLI o GUI)
     * @param nickname The nickname of the client
     * @throws IOException If an I/O error occurs during the process
     */
    void presentation(InterfaceClient cl, String nickname) throws IOException;

    /**
     * Sends the lobby parameters to the client and handles the lobby creation process.
     *
     * @param cl The client interface (GUI o CLI)
     * @param maxPlayerNumber The maximum number of players allowed in the lobby
     * @param onlyOneCommonCard A boolean indicating if there will be one common card or two common cards
     * @throws RemoteException If a remote error occurs during the process
     */
    void sendParameters(InterfaceClient cl,int maxPlayerNumber,boolean onlyOneCommonCard) throws RemoteException;
    /**
     * Executes the move received from the client and updates the game state.
     *
     * @param move The move object representing the player's move
     * @throws RemoteException If the move is invalid by the game rules
     */
    void executeMove(Body move) throws RemoteException;
    /**
     * Ping called from the client to server only to check for RemoteExceptions
     * @throws RemoteException A throw message used for ping purpose.
     */
    void clearRMI() throws RemoteException;
    /**
     * Handles the peer-to-peer message from the sender to the receiver.
     *
     * @param sender The nickname of the sender
     * @param receiver The nickname of the receiver
     * @param text The message text
     * @param localDateTime The timestamp of the message
     * @throws RemoteException If the nickname of the receiver is misspelled
     */
    void peerToPeerMsgHandler(String sender, String receiver, String text, String localDateTime) throws RemoteException;
    /**
     * Handles the broadcast message from the sender to all connected clients.
     *
     * @param sender The nickname of the sender
     * @param text The message text
     * @param localDateTime The timestamp of the message
     * @throws RemoteException If a remote error occurs during the process
     */
    void broadcastMsgHandler(String sender, String text, String localDateTime) throws RemoteException;
    /**
     * Handles intentional disconnection of a RMI user.
     *
     * @param nickname The nickname of the player that wants to disconnect.
     * @throws RemoteException If a remote error occurs during the process
     */
    void voluntaryDisconnection(String nickname) throws RemoteException;
}
