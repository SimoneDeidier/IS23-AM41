package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.Player;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TCPMessageController {

    private final GameController gameController;
    private final Socket socket;
    private final SerializeDeserialize serializeDeserialize;

    public TCPMessageController(SocketManager socketManager, SerializeDeserialize serializeDeserialize) {
        this.socket = socketManager.getSocket();
        this.gameController = GameController.getGameController(socket, this, true);
        this.serializeDeserialize = serializeDeserialize;
    }

    public void readTCPMessage(TCPMessage message) {
        String header = message.getHeader();
        System.out.printf("New TCP message - header: " + header);
        switch (header) {
            case "Helo" -> {
                if(gameController.getAvailableSlot() == -1 || gameController.getAvailableSlot() > 0) {
                    // primo giocatore o ci sono slot per altri giocatori
                    printTCPMessage("Connected", null);
                    return;
                }
                else {
                    // la lobby è piena
                    printTCPMessage("Goodbye", null);
                    return;
                }
            }
            case "Presentation" -> {
                if(gameController.getAvailableSlot() == -1) {
                    // aggiunge questo giocatore
                    // todo manca il check sul salvataggio
                    Player player = new Player(message.getBody().getPlayerNickname());

                    gameController.addPlayer(player);
                    gameController.changeState(new WaitingForPlayerState());
                    printTCPMessage("Get Parameters", null);
                }
                else if(gameController.getAvailableSlot() > 0) {
                    for(String s : gameController.getConnectedUsersNicks()) {
                        if(Objects.equals(message.getBody().getPlayerNickname(), s)) {
                            printTCPMessage("Invalid Nickname", null);
                            return;
                        }
                    }
                    // todo c'è da implementare tutta la logica dei salvataggi!!!
                    printTCPMessage("Nickname Accepted", null);
                    gameController.addPlayer(new Player(message.getBody().getPlayerNickname()));
                    if(gameController.getAvailableSlot() == 0) {
                        printTCPMessage("Game Start", null);
                        return;
                    }
                }
                else {
                    printTCPMessage("Goodbye", null);
                    return;
                }
            }
            case "Create Lobby" -> {
                int players = message.getBody().getPlayers();
                if(players < 2 || players > 4) {
                    printTCPMessage("Wrong Parameters", null);
                    return;
                }
                else {
                    gameController.setupGame(message.getBody().isTwoCommon());
                    printTCPMessage("Lobby Created", null);
                    return;
                }
            }
            case "Disconnect" -> {
                serializeDeserialize.closeConnection();
                printTCPMessage("Goodbye", null);
                return;
            }
            case "Move" -> {
                return;
            }
            case "Broadcast Message" -> {
                String text = message.getBody().getText();
                for(Socket s : GameController.getSocketMapping().keySet()) {
                    Body body = new Body();
                    body.setText(text);
                    GameController.getSocketMapping().get(s).printTCPMessage("Broadcast Msg", body);
                }
            }
            case "Peer-to-Peer Msg" -> {
                String text = message.getBody().getText();
                String senderNick = message.getBody().getSenderNickname();
                String receiverNick = message.getBody().getReceiverNickname();

            }
        }
    }

    public void printTCPMessage(String header, Body body) {
        TCPMessage newMsg = new TCPMessage(header, body);
        serializeDeserialize.serialize(newMsg);
    }

}
