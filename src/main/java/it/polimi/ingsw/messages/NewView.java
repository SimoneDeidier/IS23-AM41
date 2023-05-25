package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NewView implements Serializable {
    private String activePlayerNickname;
    private boolean gameOver;
    private Map<String, Item[][]> nicknameToShelfMap = new LinkedHashMap<>(4);
    private Map<String, Integer> nicknameToPointsMap = new LinkedHashMap<>(4);
    private Item[][] boardItems;
    private boolean[][] boardBitMask;
    private List<String> playerList = new ArrayList<>(4);

    public String getActivePlayer() {
        return activePlayerNickname;
    }

    public void setActivePlayer(String activePlayerNickname) {
            this.activePlayerNickname = activePlayerNickname;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Map<String, Item[][]> getNicknameToShelfMap() {
        return nicknameToShelfMap;
    }

    public void setNicknameToShelfMap(Map<String, Item[][]> nicknameToShelfMap) {
        this.nicknameToShelfMap = nicknameToShelfMap;
    }

    public Map<String, Integer> getNicknameToPointsMap() {
        return nicknameToPointsMap;
    }

    public void setNicknameToPointsMap(Map<String, Integer> nicknameToPointsMap) {
        this.nicknameToPointsMap = nicknameToPointsMap;
    }

    public Item[][] getBoardItems() {
        return boardItems;
    }

    public void setBoardItems(Item[][] boardItems) {
        this.boardItems = boardItems;
    }

    public boolean[][] getBoardBitMask() {
        return boardBitMask;
    }

    public void setBoardBitMask(boolean[][] boardBitMask) {
        this.boardBitMask = boardBitMask;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }
}
