package it.polimi.ingsw.servercotroller;

public class TCPMessageController {

    private static TCPMessageController instance;
    private final GameController gameController;

    private TCPMessageController() {
        this.gameController = GameController.getGameController();
    }

    public static TCPMessageController getTCPMessageController() {
        if(instance == null) {
            instance = new TCPMessageController();
        }
        return instance;
    }

    

}
