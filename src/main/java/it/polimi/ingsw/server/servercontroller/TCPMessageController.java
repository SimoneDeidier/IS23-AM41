package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Objects;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final GameController gameController;
    private final Socket socket;
    private final SerializeDeserialize serializeDeserialize;

    public TCPMessageController(SocketManager socketManager, SerializeDeserialize serializeDeserialize) {
        this.socket = socketManager.getSocket();
        this.gameController = GameController.getGameController(null);
        this.serializeDeserialize = serializeDeserialize;
    }

    @Override
    public void readTCPMessage(TCPMessage message) throws RemoteException {
        String header = message.getHeader();
        switch (header) {
            case "Presentation" -> {
                String nickname = message.getBody().getPlayerNickname();
                try {
                    switch(gameController.presentation(nickname)) {
                        case 1 -> { //joined a "new" game
                            printTCPMessage("Nickname Accepted", null);
                            gameController.putNickToSocketMapping(nickname, this);
                        } case 3 -> { //joined a "restored" game
                            printTCPMessage("Player Restored", null);
                            gameController.putNickToSocketMapping(nickname, this);
                        }
                        case 0 -> {  // you're joining but I need another nickname
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
                    System.out.println("REJOIN REQUEST");
                    System.out.println("VAL: " + gameController.didLastUserMadeHisMove());
                    gameController.changePlayerConnectionStatus(nickname);
                    printTCPMessage("Rejoined", null);
                    Body body = new Body();
                    for(Player p : gameController.getPlayerList()) {
                        if(Objects.equals(p.getNickname(), nickname)) {
                            body.setPersonalCardNumber(p.getPersonalTargetCard().getPersonalNumber());
                        }
                    }
                    for(CommonTargetCard c : gameController.getCommonTargetCardsList()) {
                        body.getCommonTargetCardsName().add(c.getName());
                    }
                    System.out.println("VAL: " + gameController.didLastUserMadeHisMove());
                    gameController.getNickToTCPMessageControllerMapping().put(nickname, this);
                    printTCPMessage("Your Target", body);
                    System.out.println("VAL: " + gameController.didLastUserMadeHisMove());
                    if(gameController.didLastUserMadeHisMove()) {
                        System.out.println("SONO ENTRATO NELL'IF");
                        gameController.setLastConnectedUserMadeHisMove(false);
                        gameController.changeActivePlayer();
                        gameController.updateView();
                        System.out.println("HO MANDATO LA UPDATE VIEW");
                    }
                }
            }
            case "Create Lobby" -> {
                int players = message.getBody().getNumberOfPlayers();
                boolean isOnlyOneCommon =  message.getBody().isOnlyOneCommon();
                if(gameController.createLobby(players, isOnlyOneCommon)) {
                    printTCPMessage("Lobby Created", null);
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

    @Override
    public void printTCPMessage(String header, Body body) {
        TCPMessage newMsg = new TCPMessage(header, body);
        serializeDeserialize.serialize(newMsg);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void closeConnection() {
        serializeDeserialize.closeConnection();
    }

}
