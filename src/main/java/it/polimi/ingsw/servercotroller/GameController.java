package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.BoardFactory;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TwoPlayersBoard;

import java.util.List;

public class GameController {

    private GameController instance;
    private final List<Player> playerList;
    private final Game game;

    private BoardFactory board;

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

    public boolean checkMove(Move move){
       if(move.getPlayer().equals(game.getActivePlayer())) {
            for(int[] choices: move.positionsPicked){
                if(game.getMaxPlayerNumber()==2){
                    board== TwoPlayersBoard.getTwoPlayersBoard();
                    //Non è corretto passare al get l'ItemsBag!
                }
            }
       }
    }

    public boolean executeMove(Move move){
        checkMove(move);
    }



    public void checkBoard() {

    }

}
