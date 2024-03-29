package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.servercontroller.controllerstates.RunningGameState;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * The TCPMessageController class handles incoming TCP messages and, based on the message header, does corresponding actions.
 */
public class TCPMessageController implements TCPMessageControllerInterface {

    private final GameController gameController;
    private final Socket socket;
    private final SerializeDeserialize serializeDeserialize;
    /**
     * Constructs a new TCPMessageController instance with the specified SocketManager and SerializeDeserialize instances.
     *
     * @param socketManager The SocketManager instance managing the socket connection.
     * @param serializeDeserialize The SerializeDeserialize instance for serializing and deserializing messages.
     */
    public TCPMessageController(SocketManager socketManager, SerializeDeserialize serializeDeserialize) {
        this.socket = socketManager.getSocket();
        this.gameController = GameController.getGameController(null);
        this.serializeDeserialize = serializeDeserialize;
    }
    /**
     * Handles the incoming TCPMessage by performing actions based on the message header.
     *
     * @param message The TCPMessage to be processed.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void readTCPMessage(TCPMessage message) throws IOException {
        String header = message.getHeader();
        switch (header) {
            case "Presentation" -> {
                String nickname = message.getBody().getPlayerNickname();
                try {
                    switch(gameController.presentation(nickname)) {
                        case 1 -> { //joined a "new" game
                            Body body = new Body();
                            body.setNumberOfPlayers(gameController.getMaxPlayerNumber());
                            for(Player p : gameController.getPlayerList()) {
                                body.getLobby().put(p.getNickname(), p.isConnected());
                            }
                            printTCPMessage("Nickname Accepted", body);
                            gameController.putNickToSocketMapping(nickname, this);
                            gameController.notifyOfConnectedUser(nickname);
                        } case 3 -> { //joined a "restored" game
                            printTCPMessage("Player Restored", null);
                            gameController.putNickToSocketMapping(nickname, this);
                        }
                        case 0 -> {  // you're joining, but I need another nickname
                            printTCPMessage("Invalid Nickname", null);
                        }
                        case 2 ->{
                            gameController.putNickToSocketMapping(nickname, this);
                            printTCPMessage("Lobby Restored",null);
                        }
                    }
                }
                catch (WaitForLobbyParametersException e) {
                    printTCPMessage("Wait for Lobby", null);
                }
                catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
                    gameController.getNickToTCPMessageControllerMapping().put(nickname, this);
                    gameController.disconnectAllUsers();
                    gameController.prepareForNewGame();
                } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
                    gameController.putNickToSocketMapping(nickname, this);
                    gameController.startGame();
                    gameController.yourTarget();
                    gameController.updateView();
                } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
                    printTCPMessage("Full Lobby", null);
                    closeConnection();
                } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
                    gameController.putNickToSocketMapping(nickname, this);
                    printTCPMessage("Get Parameters", null);
                }
                catch (RejoinRequestException e) {
                    gameController.changePlayerConnectionStatus(nickname);
                    gameController.putNickToSocketMapping(nickname, this);
                    if(gameController.getState().getClass().equals(RunningGameState.class)) {
                        printTCPMessage("Rejoined", null);
                        Body body = new Body();
                        for (Player p : gameController.getPlayerList()) {
                            if (Objects.equals(p.getNickname(), nickname)) {
                                body.setPersonalCardNumber(p.getPersonalTargetCard().getPersonalNumber());
                            }
                        }
                        for (CommonTargetCard c : gameController.getCommonTargetCardsList()) {
                            body.getCommonTargetCardsName().add(c.getName());
                        }
                        gameController.getNickToTCPMessageControllerMapping().put(nickname, this);
                        printTCPMessage("Your Target", body);
                        if (gameController.didLastUserMadeHisMove()) {
                            gameController.setLastConnectedUserMadeHisMove(false);
                            gameController.changeActivePlayer();
                            gameController.updateView();
                        }
                        gameController.notifyOfReconnectionAllUsers(nickname);
                    }
                    else {
                        gameController.notifyOfReconnectionInLobby(nickname);
                    }
                }
            }
            case "Create Lobby" -> {
                int players = message.getBody().getNumberOfPlayers();
                boolean isOnlyOneCommon =  message.getBody().isOnlyOneCommon();
                if(gameController.createLobby(players, isOnlyOneCommon)) {
                    Body body = new Body();
                    body.setNumberOfPlayers(gameController.getMaxPlayerNumber());
                    for(Player p : gameController.getPlayerList()) {
                        body.getLobby().put(p.getNickname(), p.isConnected());
                    }
                    printTCPMessage("Lobby Created", body);
                }
                else {
                    printTCPMessage("Wrong Parameters", null);
                }
            }
            case "Move" -> {
                try {
                    gameController.executeMove(message.getBody());
                    gameController.updateView();
                } catch (InvalidMoveException e) {
                    printTCPMessage("Incorrect Move", null);
                }

            }
            case "Peer-to-Peer Msg" -> {
                String sender = message.getBody().getSenderNickname();
                String receiver = message.getBody().getReceiverNickname();
                String text = message.getBody().getText();
                String localDateTime = message.getBody().getLocalDateTime();
                try {
                    gameController.peerToPeerMsg(sender, receiver, text, localDateTime);
                }
                catch (InvalidNicknameException e) {
                    printTCPMessage("Wrong Receiver", null);
                }
            }
            case "Broadcast Msg" -> {
                String sender = message.getBody().getSenderNickname();
                String text = message.getBody().getText();
                String localDateTime = message.getBody().getLocalDateTime();
                gameController.broadcastMsg(sender, text, localDateTime);
            }
            case "Disconnect" -> {
                gameController.intentionalDisconnectionUserTCP(this);
                serializeDeserialize.closeConnection();
            }
            case "Clear" -> {
                gameController.resetUnansweredCheckCounter(this);
            }
        }
    }
    /**
     * Sends a TCPMessage with the specified header and body to the client.
     *
     * @param header The header of the TCPMessage.
     * @param body The body of the TCPMessage.
     */
    @Override
    public void printTCPMessage(String header, Body body) {
        TCPMessage newMsg = new TCPMessage(header, body);
        serializeDeserialize.serialize(newMsg);
    }
    /**
     * Returns the Socket instance representing the socket connection.
     *
     * @return The Socket instance.
     */
    public Socket getSocket() {
        return this.socket;
    }
    /**
     * Closes the socket connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void closeConnection() throws IOException {
        serializeDeserialize.closeConnection();
    }

}
