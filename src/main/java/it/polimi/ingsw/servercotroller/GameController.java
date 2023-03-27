package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class GameController {

    private GameController instance;
    private final List<Player> playerList;
    private final Game game;

    private GameController(List<Player> playerList, Game game) {
        this.playerList = playerList;
        this.game = game;
    }

    public GameController getGameController(List<Player> playerList, Game game) {
        if (instance == null) {
            instance = new GameController(playerList, game);
        }
        return instance;
    }

    public void checkBoard() {

    }

}
