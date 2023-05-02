package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class TCPMessageController {

    private final GameController gameController;
    private final List<SocketManager> socketManagers = new ArrayList<>();
    private List<TCPMessage> list;

    public TCPMessageController(SocketManager socketManager) {
        this.gameController = GameController.getGameController();
        this.socketManagers.add(socketManager);
    }

    public List<TCPMessage> executeTCPMessage(TCPMessage message) {
        String header = message.getHeader();
        switch (header) {
            case "Helo" -> {
                list = new ArrayList<>();
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
                list = new ArrayList<>();
                if(gameController.getAvailableSlot() == -1) {
                    // aggiunge questo giocatore
                    Player player = new Player(message.getBody().getPlayerNickname());
                    gameController.addPlayer();
                    list.add(new TCPMessage("Get Parameters", null));
                }
                else if(gameController.getAvailableSlot() > 0) {
                    // check il nickname non è già usato da un altro giocatore
                    // ...
                }
                return list;
            }
            case "Disconnect" -> {
                //socketManagers.closeConnection();
                list = new ArrayList<>();
                list.add(new TCPMessage("Goodbye", null));
                return list;
            }
            case "Move" -> {

            }
        }
        return null;
    }
    

}
