package it.polimi.ingsw.Save;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.servercontroller.controllerstates.GameState;

import java.util.List;

public class Save {
    private Player activePlayer;
    private boolean lastTurn;
    private boolean gameOver;
    private GameState state;
    private List<Player> playerList;
    private List<CommonTargetCard> commonTargetCardList;
    private BoardFactory board;

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public List<CommonTargetCard> getCommonTargetCardList() {
        return commonTargetCardList;
    }

    public void setCommonTargetCardList(List<CommonTargetCard> commonTargetCardList) {
        this.commonTargetCardList = commonTargetCardList;
    }

    public BoardFactory getBoard() {
        return board;
    }

    public void setBoard(BoardFactory board) {
        this.board = board;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
