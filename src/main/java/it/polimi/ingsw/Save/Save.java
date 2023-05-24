package it.polimi.ingsw.Save;

import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.servercontroller.controllerstates.GameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Save {
    private String activePlayerNickname;
    private boolean lastTurn;
    private boolean gameOver;
    private GameState state;
    private Map<String, Shelf> nicknameToShelfMap = new HashMap<>(4);
    private Map<String, Integer> nicknameToPointsMap = new HashMap<>(4);
    private Map<String, PersonalTargetCard> nicknameToPersonalTargetCardMap = new HashMap<>(4);
    private List<CommonTargetCard> commonTargetCardList;
    private BoardFactory board;

    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }

    public void setActivePlayerNickname(String activePlayerNickname) {
        this.activePlayerNickname = activePlayerNickname;
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

    public Map<String, Shelf> getNicknameToShelfMap() {
        return nicknameToShelfMap;
    }

    public void setNicknameToShelfMap(Map<String, Shelf> nicknameToShelfMap) {
        this.nicknameToShelfMap = nicknameToShelfMap;
    }

    public Map<String, Integer> getNicknameToPointsMap() {
        return nicknameToPointsMap;
    }

    public void setNicknameToPointsMap(Map<String, Integer> nicknameToPointsMap) {
        this.nicknameToPointsMap = nicknameToPointsMap;
    }

    public Map<String, PersonalTargetCard> getNicknameToPersonalTargetCardMap() {
        return nicknameToPersonalTargetCardMap;
    }

    public void setNicknameToPersonalTargetCardMap(Map<String, PersonalTargetCard> nicknameToPersonalTargetCardMap) {
        this.nicknameToPersonalTargetCardMap = nicknameToPersonalTargetCardMap;
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
}
