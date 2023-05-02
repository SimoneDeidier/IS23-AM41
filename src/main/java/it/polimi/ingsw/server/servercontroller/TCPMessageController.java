package it.polimi.ingsw.server.servercontroller;

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
                list.add(new TCPMessage("Connected", null));
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
