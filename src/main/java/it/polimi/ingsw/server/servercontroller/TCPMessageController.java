package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.net.Socket;
import java.rmi.RemoteException;

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
        System.out.printf("New TCP message - header: " + header);
        switch (header) {
            case "Presentation" -> {
                String nickname = message.getBody().getPlayerNickname();
                System.out.println("NEW PRESENTATION MSG - " + nickname);
                try {
                    switch(gameController.presentation(nickname)) {
                        case 1: { //joined a "new" game
                            printTCPMessage("Nickname Accepted", null);
                            gameController.putNickToSocketMapping(nickname, this);
                        } case 2: { //joined a "restored" game
                            printTCPMessage("Player Restored", null);
                            gameController.putNickToSocketMapping(nickname, null);
                        }
                        case 0: {  // you're joining but I need another nickname
                            printTCPMessage("Invalid Nickname", null);
                        }
                    }
                } catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
                    gameController.disconnectAllUsers();
                } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
                    gameController.startGame();
                    gameController.yourTarget();
                    gameController.updateView();
                } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
                    printTCPMessage("Goodbye", null);
                    closeConnection();
                } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
                    printTCPMessage("Get Parameters", null);
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
                if(gameController.checkMove(message.getBody())) {
                    gameController.updateView();
                }
                else {
                    printTCPMessage("Incorrect Move", null);
                }
            }
            case "Peer-to-Peer Msg" -> {
                String sender = message.getBody().getSenderNickname();
                String receiver = message.getBody().getReceiverNickname();
                String text = message.getBody().getText();
                try {
                    gameController.peerToPeerMsg(sender, receiver, text);
                }
                catch (InvalidNicknameException e) {
                    printTCPMessage("Wrong Receiver", null);
                }
            }
            case "Broadcast Msg" -> {
                String sender = message.getBody().getSenderNickname();
                String text = message.getBody().getText();
                gameController.broadcastMsg(sender, text);
            }
            case "Disconnect" -> {
                gameController.disconnectUserTCP(this);
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
