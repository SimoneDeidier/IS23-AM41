package it.polimi.ingsw.servercotroller;

public class TCPMessageController {

    private TCPMessageController instance;
    private final GameController gameController;

    private TCPMessageController(GameController gameController) {
        this.gameController = gameController;
    }

    public TCPMessageController getTCPMessageController(GameController gameController) {
        if(instance == null) {
            instance = new TCPMessageController(gameController);
        }
        return instance;
    }



}
