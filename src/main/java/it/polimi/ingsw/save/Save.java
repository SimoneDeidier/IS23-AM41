package it.polimi.ingsw.save;

import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import it.polimi.ingsw.server.servercontroller.controllerstates.GameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Save {
    private String activePlayerNickname;
    private Map<String, Item[][]> nicknameToShelfMap = new HashMap<>(4);
    private Map<String, Integer> nicknameToPointsMap = new HashMap<>(4);
    private Map<String, List<ScoringToken>> nicknameToScoringTokensMap = new HashMap<>(4);
    private Map<String, PersonalTargetCard> nicknameToPersonalTargetCard=new HashMap<>(4);
    private String endGameTokenAssignedToWhom;
    private boolean lastTurn;
    private int maxPlayerPlayer;
    private boolean gameOver;
    private Map<String,List<ScoringToken>> commonTargetCardMap=new HashMap<>(4);;
    private Item[][] boardItems;
    private boolean[][] boardBitMask;

    public Save() {
        this.activePlayerNickname=null;
        this.endGameTokenAssignedToWhom = null;
    }

    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }

    public void setActivePlayerNickname(String activePlayerNickname) {
        this.activePlayerNickname = activePlayerNickname;
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

    public Map<String, List<ScoringToken>> getNicknameToScoringTokensMap() {
        return nicknameToScoringTokensMap;
    }

    public void setNicknameToScoringTokensMap(Map<String, List<ScoringToken>> nicknameToScoringTokensMap) {
        this.nicknameToScoringTokensMap = nicknameToScoringTokensMap;
    }

    public Map<String, PersonalTargetCard> getNicknameToPersonalTargetCard() {
        return nicknameToPersonalTargetCard;
    }

    public void setNicknameToPersonalTargetCard(Map<String, PersonalTargetCard> nicknameToPersonalTargetCard) {
        this.nicknameToPersonalTargetCard = nicknameToPersonalTargetCard;
    }

    public String getEndGameTokenAssignedToWhom() {
        return endGameTokenAssignedToWhom;
    }

    public void setEndGameTokenAssignedToWhom(String endGameTokenAssignedToWhom) {
        this.endGameTokenAssignedToWhom = endGameTokenAssignedToWhom;
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

    public Map<String, List<ScoringToken>> getCommonTargetCardMap() {
        return commonTargetCardMap;
    }

    public void setCommonTargetCardMap(Map<String, List<ScoringToken>> commonTargetCardMap) {
        this.commonTargetCardMap = commonTargetCardMap;
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

    public int getMaxPlayerPlayer() {
        return maxPlayerPlayer;
    }

    public void setMaxPlayerPlayer(int maxPlayerPlayer) {
        this.maxPlayerPlayer = maxPlayerPlayer;
    }
}
