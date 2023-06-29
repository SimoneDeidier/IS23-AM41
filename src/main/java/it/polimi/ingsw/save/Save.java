package it.polimi.ingsw.save;

import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.ScoringToken;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * The `Save` class represents the saved state of the game. It contains all information for restoring an unfinished game.
 */

public class Save {
    private String activePlayerNickname;
    private Map<String, Item[][]> nicknameToShelfMap = new LinkedHashMap<>(4);
    private Map<String, Integer> nicknameToPointsMap = new LinkedHashMap<>(4);
    private Map<String, List<ScoringToken>> nicknameToScoringTokensMap = new LinkedHashMap<>(4);
    private Map<String, PersonalTargetCard> nicknameToPersonalTargetCard=new LinkedHashMap<>(4);
    private String endGameTokenAssignedToWhom;
    private boolean lastTurn;
    private int maxPlayerPlayer;
    private boolean gameOver;
    private Map<String,List<ScoringToken>> commonTargetCardMap=new LinkedHashMap<>(4);
    private Item[][] boardItems;
    private boolean[][] boardBitMask;
    /**
     * Constructs a new and clean Save.
     */
    public Save() {
        this.activePlayerNickname=null;
        this.endGameTokenAssignedToWhom = null;
    }
    /**
     * Gives the nickname of the active player.
     *
     * @return The active player's nickname.
     */
    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }
    /**
     * Sets the nickname of the active player.
     *
     * @param activePlayerNickname The nickname of the active player.
     */
    public void setActivePlayerNickname(String activePlayerNickname) {
        this.activePlayerNickname = activePlayerNickname;
    }
    /**
     * Gives the map of player nicknames to their shelves.
     *
     * @return The map of player nicknames to shelves.
     */
    public Map<String, Item[][]> getNicknameToShelfMap() {
        return nicknameToShelfMap;
    }
    /**
     * Maps player nicknames to their shelves.
     *
     * @param nicknameToShelfMap The map of player nicknames to shelves.
     */
    public void setNicknameToShelfMap(Map<String, Item[][]> nicknameToShelfMap) {
        this.nicknameToShelfMap = nicknameToShelfMap;
    }
    /**
     * Gives the map of player nicknames to their points.
     *
     * @return The map of player nicknames to points.
     */
    public Map<String, Integer> getNicknameToPointsMap() {
        return nicknameToPointsMap;
    }
    /**
     * Maps the player nicknames to their points.
     *
     * @param nicknameToPointsMap The map of player nicknames to points.
     */
    public void setNicknameToPointsMap(Map<String, Integer> nicknameToPointsMap) {
        this.nicknameToPointsMap = nicknameToPointsMap;
    }
    /**
     * Gives the map of player nicknames to their scoring tokens.
     *
     * @return The map of player nicknames to scoring tokens.
     */
    public Map<String, List<ScoringToken>> getNicknameToScoringTokensMap() {
        return nicknameToScoringTokensMap;
    }
    /**
     * Maps the player nicknames to their scoring tokens.
     *
     * @param nicknameToScoringTokensMap The map of player nicknames to scoring tokens.
     */
    public void setNicknameToScoringTokensMap(Map<String, List<ScoringToken>> nicknameToScoringTokensMap) {
        this.nicknameToScoringTokensMap = nicknameToScoringTokensMap;
    }
    /**
     * Gives the map of player nicknames to their personal target cards.
     *
     * @return The map of player nicknames to personal target cards.
     */
    public Map<String, PersonalTargetCard> getNicknameToPersonalTargetCard() {
        return nicknameToPersonalTargetCard;
    }
    /**
     * Maps the player nicknames to their personal target cards.
     *
     * @param nicknameToPersonalTargetCard The map of player nicknames to personal target cards.
     */
    public void setNicknameToPersonalTargetCard(Map<String, PersonalTargetCard> nicknameToPersonalTargetCard) {
        this.nicknameToPersonalTargetCard = nicknameToPersonalTargetCard;
    }
    /**
     * Gives the nickname of the player assigned the end game token.
     *
     * @return The nickname of the player assigned the end game token.
     */
    public String getEndGameTokenAssignedToWhom() {
        return endGameTokenAssignedToWhom;
    }
    /**
     * Sets the nickname of the player that has got the end game token.
     *
     * @param endGameTokenAssignedToWhom The nickname of the player that has got the end game token.
     */
    public void setEndGameTokenAssignedToWhom(String endGameTokenAssignedToWhom) {
        this.endGameTokenAssignedToWhom = endGameTokenAssignedToWhom;
    }
    /**
     * Checks if it is the last turn in the game.
     *
     * @return true if it is the last turn, false otherwise.
     */
    public boolean isLastTurn() {
        return lastTurn;
    }
    /**
     * Sets whether it is the last turn in the game.
     *
     * @param lastTurn true if it is the last turn, false otherwise.
     */
    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }
    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }
    /**
     * Sets the boolean flag that represent if the game is over.
     *
     * @param gameOver true if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    /**
     * Gives the map of common target card names to their scoring tokens.
     *
     * @return The map of common target card names to scoring tokens.
     */
    public Map<String, List<ScoringToken>> getCommonTargetCardMap() {
        return commonTargetCardMap;
    }
    /**
     * Mpas common target card names to their scoring tokens.
     *
     * @param commonTargetCardMap The map of common target card names to scoring tokens.
     */
    public void setCommonTargetCardMap(Map<String, List<ScoringToken>> commonTargetCardMap) {
        this.commonTargetCardMap = commonTargetCardMap;
    }

    /**
     * Gives the game board items.
     *
     * @return The game board items.
     */
    public Item[][] getBoardItems() {
        return boardItems;
    }
    /**
     * Sets the game board items.
     *
     * @param boardItems The game board items.
     */
    public void setBoardItems(Item[][] boardItems) {
        this.boardItems = boardItems;
    }
    /**
     * Gives the bitmask representing the state of the game board.
     *
     * @return The bitmask representing the state of the game board.
     */
    public boolean[][] getBoardBitMask() {
        return boardBitMask;
    }
    /**
     * Sets the bitmask representing the state of the game board.
     *
     * @param boardBitMask The bitmask representing the state of the game board.
     */
    public void setBoardBitMask(boolean[][] boardBitMask) {
        this.boardBitMask = boardBitMask;
    }
    /**
     * Gives the maximum number of players allowed in the game.
     *
     * @return The maximum number of players allowed in the game.
     */
    public int getMaxPlayerPlayer() {
        return maxPlayerPlayer;
    }
    /**
     * Sets the maximum number of players allowed in the game.
     *
     * @param maxPlayerPlayer The maximum number of players allowed in the game.
     */
    public void setMaxPlayerPlayer(int maxPlayerPlayer) {
        this.maxPlayerPlayer = maxPlayerPlayer;
    }
}
