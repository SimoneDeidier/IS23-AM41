package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.controllerstates.WaitingForPlayerState;
import it.polimi.ingsw.server.servercontroller.exceptions.CancelGameException;
import it.polimi.ingsw.server.servercontroller.exceptions.FirstPlayerException;
import it.polimi.ingsw.server.servercontroller.exceptions.FullLobbyException;
import it.polimi.ingsw.server.servercontroller.exceptions.GameStartException;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;
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
        System.out.printf("New TCP message - header: " + header);
        switch (header) {
            case "Presentation" -> {
                /*if(gameController.getAvailableSlot() == -1) {
                    // aggiunge questo giocatore
                    // todo manca il check sul salvataggio
                    String nickname = message.getBody().getPlayerNickname();
                    Player player = new Player(nickname);
                    gameController.addPlayer(player);
                    gameController.putNickToSocketMapping(nickname, this);
                    gameController.changeState(new WaitingForPlayerState());
                    printTCPMessage("Get Parameters", null);
                }
                else if(gameController.getAvailableSlot() > 0) {
                    String nickname = message.getBody().getPlayerNickname();
                    for(String s : gameController.getConnectedUsersNicks()) {
                        if(Objects.equals(nickname, s)) {
                            printTCPMessage("Invalid Nickname", null);
                            return;
                        }
                    }
                    // todo c'è da implementare tutta la logica dei salvataggi!!!
                    printTCPMessage("Nickname Accepted", null);
                    gameController.addPlayer(new Player(nickname));
                    gameController.putNickToSocketMapping(nickname, this);
                    if(gameController.getAvailableSlot() == 0) {
                        printTCPMessage("Game Start", null);
                    }
                }
                else {
                    printTCPMessage("Goodbye", null);
                } */
                String nickname = message.getBody().getPlayerNickname();
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
                    /*for (Map.Entry<String,InterfaceClient> entry : clientMapRMI.entrySet()) {
                        entry.getValue().updateView(controller.generateUpdatedView());
                        entry.getValue().updatePersonalView(controller.generateUpdatedPersonal(entry.getKey()));
                    }
                    INVIARE LA PERSONAL
                    UPDATEVIEW
                    */
                    gameController.sendPersonalTargetCards();
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
                if(players < 2 || players > 4) {
                    printTCPMessage("Wrong Parameters", null);
                }
                else {
                    gameController.setupGame(message.getBody().isOnlyOneCommon());
                    printTCPMessage("Lobby Created", null);
                }
            }
            case "Disconnect" -> {
                String nickname = null;
                for(String s : gameController.getNickToTCPMessageControllerMapping().keySet()) {
                    if(gameController.getNickToTCPMessageControllerMapping().get(s) == this) {
                        nickname = s;
                    }
                }
                for(Player p : gameController.getPlayerList()) {
                    if(Objects.equals(p.getNickname(), nickname)) {
                        p.setConnected(false);
                    }
                }
                serializeDeserialize.closeConnection();
                printTCPMessage("Goodbye", null);
            }
            case "Move" -> {
                // todo
            }
            case "Broadcast Message" -> {
                String text = message.getBody().getText();
                for(String s : gameController.getNickToTCPMessageControllerMapping().keySet()) {
                    Body body = new Body();
                    body.setText(text);
                    gameController.getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Broadcast Msg", body);
                }
            }
            case "Peer-to-Peer Msg" -> {
                String text = message.getBody().getText();
                String senderNick = message.getBody().getSenderNickname();
                String receiverNick = message.getBody().getReceiverNickname();
                boolean receiverFound = false;
                for(String s : gameController.getNickToTCPMessageControllerMapping().keySet()) {
                    if (Objects.equals(s, receiverNick)) {
                        Body body = new Body();
                        body.setText(text);
                        gameController.getNickToTCPMessageControllerMapping().get(s).printTCPMessage("Peer-to-Peer Msg", body);
                        receiverFound = true;
                    }
                }
                if(receiverFound) {
                    Body body = new Body();
                    body.setText(text);
                    gameController.getNickToTCPMessageControllerMapping().get(senderNick).printTCPMessage("Peer-to-Peer Msg", body);
                }
                else {
                    gameController.getNickToTCPMessageControllerMapping().get(senderNick).printTCPMessage("Invalid Nickname", null);
                }
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
