package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerRMI;
import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * The ConnectionRMI class manages the connection of RMI clients to the server.
 */
public class ConnectionRMI extends UnicastRemoteObject implements InterfaceClient, Serializable, Connection {
    private final int PORT;
    private final String IP;
    private InterfaceServer stub;
    private ClientController controller;
    private static final int CLEAR_DELAY_MILLISECONDS = 5000;
    private boolean clientConnected;
    private boolean wasIJustReconnected =false;
    private boolean alreadySetMyCards=false;

    /**
     * Constructs a new ConnectionRMI with the specified parameters.
     * @param port The number of the port
     * @param IP The ip address
     * @throws RemoteException  If a remote error occurs during the process
     */
    public ConnectionRMI(int port, String IP) throws RemoteException {
        super();
        this.PORT = port;
        this.IP = IP;
    }
    /**
     * Starts the RMI connection.
     *
     * @param uiType the type of user interface to start (GUI o CLI)
     */
    @Override
    public void startConnection(String uiType) {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(IP, PORT);
            // Looking up the registry for the remote object
            stub = (InterfaceServer) registry.lookup("InterfaceServer");
            clientConnected=true;
            controller = new ClientControllerRMI(this);
            controller.startUserInterface(uiType);
        } catch (Exception e) {
            System.out.println("Server is offline or unreachable");
        }
    }
    /**
     * Sends the player's nickname to the server for presentation.
     *
     * @param nickname the player's nickname
     */
    public void presentation(String nickname) {
        try {
            stub.presentation(this, nickname);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        } catch (IOException e) {
            System.err.println("An unknown exception was thrown. If the problem persists, please restart the client!");
        }
    }
    /**
     * Asks the server controller for the game parameters.
     *
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public void askParameters() throws RemoteException {
        controller.getParameters();
    }
    /**
     * Sends the game parameters to the server controller.
     *
     * @param maxPlayerNumber the maximum number of players in the game
     * @param onlyOneCommon boolean flag rappresenting the number of commoncards: true if there is only one common goal card, false if there are two
     */
    public void sendParameters(int maxPlayerNumber, boolean onlyOneCommon) {
        try {
            stub.sendParameters(this, maxPlayerNumber, onlyOneCommon);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }
    /**
     * Asks the user for a new nickname and send it to the server controller.
     *
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public void askForNewNickname() throws RemoteException {
        controller.invalidNickname();
    }
    /**
     * Updates the client's view with the new view received from the server.
     *
     * @param newView the new view
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public void updateView(NewView newView) throws RemoteException {
        if(wasIJustReconnected){
            try {
                controller.loadGameScreen();
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            wasIJustReconnected=false;
        }
        try {
            controller.updateView(newView);
        } catch (FileNotFoundException | URISyntaxException e) {
            System.out.println("Unknown problem loading the game screen!");
        }
    }

    /**
     * Print the correct message after the disconnection of a user based on the message input
     *
     * @param whichMessageToShow the message to show
     * @throws RemoteException if a remote exception occurs
     */
    @Override
    public void disconnectUser(int whichMessageToShow) throws RemoteException {
        clientConnected=false; //stops the ping thread

        switch(whichMessageToShow){
            case 0 -> {
                try {
                    controller.cantRestoreLobby();
                } catch (IOException e) {
                    System.err.println("A crash occurred when loading the scene, please restart the software!");
                }
            }
            case 1 -> {
                controller.alonePlayerWins();
            }
            default -> System.err.println("INCORRECT GOODBYE TCP MESSAGE!");
        }

    }

    /**
     * Confirms the connection with the server.
     *
     * @param typeOfLobby The type of lobby.
     * @param lobby map representing the lobby
     * @param nPlayers number of players
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void confirmConnection(boolean typeOfLobby, int nPlayers, Map<String, Boolean> lobby) throws RemoteException {
        startClearThread();
        if (typeOfLobby) {
            controller.playerRestored();
        } else {
            controller.nicknameAccepted(nPlayers, lobby);
        }
    }
    /**
     * Receives a message from the server.
     *
     * @param sender The sender of the message.
     * @param message The message received.
     * @param localDateTime The timestamp of the message.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void receiveMessage(String sender, String message, String localDateTime) throws RemoteException {
        controller.receiveMessage(message, sender, localDateTime);
    }
    /**
     * Notifies the client about receiving a wrong message.
     *
     * @param message The wrong message received.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void wrongMessageWarning(String message) throws RemoteException {
        controller.wrongReceiver();
    }
    /**
     * Receives the cards from the server.
     *
     * @param whichPersonal        The number of the personal target card.
     * @param commonTargetCardList The list of common goal cards.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException {
        if(!alreadySetMyCards) {
            controller.setPersonalTargetCardNumber(whichPersonal);
            controller.setCommonGoalList(commonTargetCardList);
            if(!wasIJustReconnected) {
                try {
                    controller.loadGameScreen();
                } catch (IOException e) {
                    System.err.println("A crash occurred when loading the scene, please restart the software!");
                }
            }
            alreadySetMyCards=true;
        }
    }
    /**
     * Notifies the client about the creation of a lobby.
     *
     * @param typeOfGame The type of the game lobby.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void lobbyCreated(boolean typeOfGame, int nPlayers, Map<String, Boolean> lobby) throws RemoteException {
        startClearThread();
        if (typeOfGame)
            controller.lobbyCreated(nPlayers, lobby);
        else
            controller.lobbyRestored();
    }
    /**
     * Waits for the creation of a lobby.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void waitForLobbyCreation() throws RemoteException {
        controller.waitForLobby();
    }
    /**
     * Asks the client to provide the parameters again.
     */
    @Override
    public void askParametersAgain() {
        controller.wrongParameters();
    }
    /**
     * Sends a private message from one client to another via the server.
     *
     * @param body The message body.
     */
    public void sendPrivateMessage(Body body) {
        try {
            stub.peerToPeerMsgHandler(body.getSenderNickname(), body.getReceiverNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }
    /**
     * Sends a broadcast message from one client to all other clients via the server.
     *
     * @param body The message body.
     */
    public void sendBroadcastMessage(Body body) {
        try {
            stub.broadcastMsgHandler(body.getSenderNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }
    /**
     * Sends a move to the server.
     *
     * @param body The move body.
     */
    public void sendMoveToServer(Body body) {
        try {
            stub.executeMove(body);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }
    /**
     * Notifies the client about an incorrect move.
     */
    @Override
    public void incorrectMove() {
        controller.incorrectMove();
    }
    /**
     * Starts a separate thread for clearing the RMI connection.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    public void startClearThread() throws RemoteException {
        new Thread(() -> {
            while (clientConnected) {
                try {
                    stub.clearRMI();
                    Thread.sleep(CLEAR_DELAY_MILLISECONDS);
                } catch (RemoteException e) {
                    controller.serverNotResponding();
                    break;
                } catch (InterruptedException e) {
                    //thread interrupted
                }
            }
        }).start();
    }
    /**
     * Checks the connection with the server (ping).
     *
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void check() throws RemoteException { //ping called from server to client
        //it's empty, we need to check on the other side for RemoteExceptions
    }
    /**
     * Sends a voluntary disconnection to the server.
     */
    public void voluntaryDisconnection(){
        try {
            stub.voluntaryDisconnection(controller.getPlayerNickname());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }
    /**
     * Notifies the client about rejoining a match.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void rejoinedMatch() throws RemoteException {
        wasIJustReconnected=true;
        controller.rejoinedMatch();
    }
    /**
     * Notifies the client about a full lobby.
     *
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void fullLobby() throws RemoteException {
        controller.fullLobby();
    }

    /**
     * Notifies the client about a player reconnection.
     * @param nickname The nickname of the reconnected player.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void notificationForReconnection(String nickname) throws RemoteException {
        controller.playerReconnected(nickname);
    }
    /**
     * Notifies the client about a player disconnection.
     * @param nickname The nickname of the disconnected player.
     * @throws RemoteException if a remote exception occurs.
     */
    @Override
    public void notificationForDisconnection(String nickname) throws RemoteException {
        controller.playerDisconnected(nickname);
    }

    /**
     * Sets the boolean flag for the status of the client connection.
     *
     * @param clientConnected the boolean value of the status of the connection.
     */
    public void setClientConnected(boolean clientConnected) {
        this.clientConnected = clientConnected;
    }

    /**
     * Closes the client connection.
     */
    @Override
    public void closeConnection() {
        System.exit(0);
    }

    /**
     * Notify the server that a new user has connected.
     *
     * @param nickname nickname of the user
     */
    @Override
    public void notifyConnectedUser(String nickname) {
        controller.userConnected(nickname);
    }

    /**
     * Notify the server that a user has disconnetted from the lobby.
     *
     * @param nickname nickname of the user
     */
    @Override
    public void disconnectedFromLobby(String nickname) throws RemoteException {
        controller.disconnectedFromLobby(nickname);
    }

    /**
     * Notify the server that a user has rejoined the lobby.
     *
     * @param lobby Map of the lobby
     * @param numPlayers number of current players
     */
    @Override
    public void rejoinedInLobby(Map<String, Boolean> lobby, int numPlayers) throws RemoteException {
        controller.nicknameAccepted(numPlayers, lobby);
    }

    /**
     * Notify the server that a user has rejoined.
     *
     * @param nickname nickname of the user
     */
    @Override
    public void userRejoined(String nickname) throws RemoteException {
        controller.userRejoined(nickname);
    }

}
