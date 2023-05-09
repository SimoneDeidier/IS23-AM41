package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.Player;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TCPMessageController {

    private final GameController gameController;
    private final SocketManager socketManager;
    private final Socket socket;

    public TCPMessageController(SocketManager socketManager) {
        this.socketManager = socketManager;
        this.socket = socketManager.getSocket();
        this.gameController = GameController.getGameController(socket, this);
    }

    public List<TCPMessage> executeTCPMessage(TCPMessage message) {
        String header = message.getHeader();
        System.out.printf("New TCP message - header: " + header);
        List<TCPMessage> list = new ArrayList<>();
        switch (header) {
            case "Helo" -> {
                if(gameController.getAvailableSlot() == -1 || gameController.getAvailableSlot() > 0) {
                    // primo giocatore o ci sono slot per altri giocatori
                    list.add(new TCPMessage("Connected", null));
                }
                else {
                    // la lobby è piena
                    list.add(new TCPMessage("Goodbye", null));
                }
                return list;
            }
            case "Presentation" -> {
                if(gameController.getAvailableSlot() == -1) {
                    // aggiunge questo giocatore
                    // todo manca il check sul salvataggio
                    Player player = new Player(message.getBody().getPlayerNickname());
                    gameController.addPlayer(player);
                    gameController.changeState(new WaitingForPlayerState());
                    list.add(new TCPMessage("Get Parameters", null));
                    return list;
                }
                else if(gameController.getAvailableSlot() > 0) {
                    for(String s : gameController.getConnectedUsersNicks()) {
                        if(Objects.equals(message.getBody().getPlayerNickname(), s)) {
                            list.add(new TCPMessage("Invalid Nickname", null));
                            return list;
                        }
                    }
                    // todo c'è da implementare tutta la logica dei salvataggi!!!
                    list.add(new TCPMessage("Nickname Accepted", null));
                    gameController.addPlayer(new Player(message.getBody().getPlayerNickname()));
                    if(gameController.getAvailableSlot() == 0) {
                        list.add(new TCPMessage("Game Start", null));
                    }
                    return list;
                }
                list.add(new TCPMessage("Goodbye", null));
                return list;
            }
            case "Create Lobby" -> {
                int players = message.getBody().getPlayers();
                if(players < 2 || players > 4) {
                    list.add(new TCPMessage("Wrong Parameters", null));
                    return list;
                }
                else {
                    gameController.setupGame(message.getBody().isTwoCommon());
                    list.add(new TCPMessage("Lobby Created", null));
                    return list;
                }
            }
            case "Disconnect" -> {
                socketManager.closeConnection();
                list.add(new TCPMessage("Goodbye", null));
                return list;
            }
            case "Move" -> {
                return null;
            }
            case "Broadcast Message" -> {
                // todo non so come fare i messaggi asincroni :(
            }
        }
        return null;
    }
    

}
