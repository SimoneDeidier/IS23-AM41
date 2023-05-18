package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Player;

import java.io.Serializable;
import java.util.List;

public class NewView implements Serializable {
    private List<Player> playerList;
    private Player activePlayer;
    private boolean gameOver;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
