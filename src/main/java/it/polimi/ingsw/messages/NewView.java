package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The NewView class represents the updated view of the game state. It contains all information to display to give to the players.
 */
public class NewView implements Serializable {
    private String activePlayerNickname;
    private boolean gameOver;
    private Map<String, Item[][]> nicknameToShelfMap = new LinkedHashMap<>(4);
    private Map<String, Integer> nicknameToPointsMap = new LinkedHashMap<>(4);
    private Item[][] boardItems;
    private boolean[][] boardBitMask;
    private List<String> playerList = new ArrayList<>(4);
    private EndGameToken endGameToken;
    private Map<String, List<ScoringToken>> playersToTokens = new LinkedHashMap<>(4);
    private Map<String, List<ScoringToken>> commonsToTokens = new LinkedHashMap<>(4);
    private boolean youAreTheLastUserAndYouAlreadyMadeYourMove;

    /**
     * Gives the nickname of the active player.
     *
     * @return The active player's nickname.
     */
    public String getActivePlayer() {
        return activePlayerNickname;
    }
    /**
     * Sets the nickname of the active player.
     *
     * @param activePlayerNickname The active player's nickname.
     */
    public void setActivePlayer(String activePlayerNickname) {
            this.activePlayerNickname = activePlayerNickname;
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
     * Sets the game over flag.
     *
     * @param gameOver true if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    /**
     * Gives the map of player nicknames to their respective shelves.
     *
     * @return The map of player nicknames to shelves.
     */
    public Map<String, Item[][]> getNicknameToShelfMap() {
        return nicknameToShelfMap;
    }
    /**
     * Maps the player's nicknames to their shelves.
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
     * Maps player's nicknames to their points.
     *
     * @param nicknameToPointsMap The map of player nicknames to points.
     */
    public void setNicknameToPointsMap(Map<String, Integer> nicknameToPointsMap) {
        this.nicknameToPointsMap = nicknameToPointsMap;
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
     * Gives the bitmask of the game board.
     *
     * @return The bitmask representing the game board state.
     */
    public boolean[][] getBoardBitMask() {
        return boardBitMask;
    }

    /**
     * Sets the bitmask representing the state of the game board.
     *
     * @param boardBitMask The bitmask representing the game board state.
     */
    public void setBoardBitMask(boolean[][] boardBitMask) {
        this.boardBitMask = boardBitMask;
    }
    /**
     * Gives the list of player nicknames.
     *
     * @return The list of player nicknames.
     */
    public List<String> getPlayerList() {
        return playerList;
    }
    /**
     * Sets the list of player nicknames.
     *
     * @param playerList The list of player nicknames.
     */
    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }
    /**
     * Gives the end game token.
     *
     * @return The end game token.
     */
    public EndGameToken getEndGameToken() {
        return endGameToken;
    }
    /**
     * Sets the end game token.
     *
     * @param endGameToken The end game token.
     */
    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }
    /**
     * Gives the map of player nicknames to their respective scoring tokens.
     *
     * @return The map of player nicknames to scoring tokens.
     */
    public Map<String, List<ScoringToken>> getPlayersToTokens() {
        return playersToTokens;
    }
    /**
     * Gives the map of common target card names to their respective scoring tokens.
     *
     * @return The map of common target card names to scoring tokens.
     */
    public Map<String, List<ScoringToken>> getCommonsToTokens() {
        return commonsToTokens;
    }
    /**
     * Checks if the current user is the last user and has already made their move.
     *
     * @return true if the current user is the last user and has already made their move, false otherwise.
     */
    public boolean youAreTheLastUserAndYouAlreadyMadeYourMove() {
        return youAreTheLastUserAndYouAlreadyMadeYourMove;
    }
    /**
     * Sets the flag representing if the current user is the last user and has already made their move.
     *
     * @param youAreTheLastUserAndYouAlreadyMadeYourMove true if the current user is the last user and has already made their move, false otherwise.
     */
    public void setYouAreTheLastUserAndYouAlreadyMadeYourMove(boolean youAreTheLastUserAndYouAlreadyMadeYourMove) {
        this.youAreTheLastUserAndYouAlreadyMadeYourMove = youAreTheLastUserAndYouAlreadyMadeYourMove;
    }
}
