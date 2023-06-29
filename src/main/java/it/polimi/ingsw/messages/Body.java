package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.PersonalTargetCard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * The Body class represents a message body used in communication player-to-server.
 */
public class Body implements Serializable {
    private String playerNickname;
    private List<int[]> positionsPicked;    //like (4 0) (4 1) (4 2), in order of insertion in the shelf
    private int column;                     //0 --> first to be inserted and so on
    private int numberOfPlayers;
    private boolean onlyOneCommon;
    private String receiverNickname;
    private String senderNickname;
    private String text;
    private int personalCardNumber;
    private NewView newView;
    private String localDateTime;
    private List<String> commonTargetCardsName = new ArrayList<>(2);
    private int goodbyeType;
    /**
     * Gives the nickname of the player associated with this message body.
     *
     * @return The player's nickname.
     */
    public String getPlayerNickname() {
        return playerNickname;
    }
    /**
     * Gives the list of positions picked, in order of insertion in the shelf.
     *
     * @return The list of positions picked.
     */
    public List<int[]> getPositionsPicked() {
        return positionsPicked;
    }
    /**
     * Gives the column associated with this message body.
     *
     * @return The column.
     */
    public int getColumn() {
        return column;
    }
    /**
     * Gives the number of players associated with this message body.
     *
     * @return The number of players.
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    /**
     * Checks flag boolean "onlyOneCommon" that is true if the number of common target card is one, false if there are two common target cards.
     *
     * @return true if the number of common target card is one, false if there are two common target cards.
     */
    public boolean isOnlyOneCommon() {
        return onlyOneCommon;
    }
    /**
     * Sets the nickname of the player associated with this message body.
     *
     * @param playerNickname The player's nickname.
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    /**
     * Sets the list of positions picked.
     *
     * @param positionsPicked The list of positions picked.
     */
    public void setPositionsPicked(List<int[]> positionsPicked) {
        this.positionsPicked = positionsPicked;
    }
    /**
     * Sets the column associated with this message body.
     *
     * @param column The column.
     */
    public void setColumn(int column) {
        this.column = column;
    }
    /**
     * Sets the number of players associated with this message body.
     *
     * @param numberOfPlayers The number of players.
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    /**
     * Sets the boolean flag indicating if the number of the common target cards.
     *
     * @param onlyOneCommon true if the number of common target card is one, false if there are two common target cards.
     */
    public void setOnlyOneCommon(boolean onlyOneCommon) {
        this.onlyOneCommon = onlyOneCommon;
    }
    /**
     * Sets the nickname of the receiver associated with this message body.
     *
     * @param receiverNickname The receiver's nickname.
     */
    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }
    /**
     * Sets the text of the message associated with this message body.
     *
     * @param text The message text.
     */
    public void setText(String text) {
        this.text = text;
    }
    /**
     * Gives the nickname of the receiver associated with this message body.
     *
     * @return The receiver's nickname.
     */
    public String getReceiverNickname() {
        return receiverNickname;
    }
    /**
     * Gives the text of the message associated with this message body.
     *
     * @return The message text.
     */
    public String getText() {
        return text;
    }
    /**
     * Gives the nickname of the sender associated with this message body.
     *
     * @return The sender's nickname.
     */
    public String getSenderNickname() {
        return senderNickname;
    }
    /**
     * Sets the nickname of the sender associated with this message body.
     *
     * @param senderNickname The sender's nickname.
     */
    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
    /**
     * Gives the personal card number associated with this message body.
     *
     * @return The personal card number.
     */
    public int getPersonalCardNumber() {
        return personalCardNumber;
    }
    /**
     * Sets the personal card number associated with this message body.
     *
     * @param personalCardNumber The personal card number.
     */
    public void setPersonalCardNumber(int personalCardNumber) {
        this.personalCardNumber = personalCardNumber;
    }

    /**
     * Gives the new view associated with this message body.
     *
     * @return The new view.
     */
    public NewView getNewView() {
        return newView;
    }
    /**
     * Sets the new view associated with this message body.
     *
     * @param newView The new view.
     */
    public void setNewView(NewView newView) {
        this.newView = newView;
    }
    /**
     * Gives the local date and time associated with this message body.
     *
     * @return The local date and time.
     */
    public String getLocalDateTime() {
        return localDateTime;
    }
    /**
     * Sets the local date and time associated with this message body.
     *
     * @param localDateTime The local date and time.
     */
    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }
    /**
     * Gives the list of common target card names associated with this message body.
     *
     * @return The list of common target card names.
     */
    public List<String> getCommonTargetCardsName() {
        return commonTargetCardsName;
    }
    /**
     * Gives the goodbye type associated with this message body.
     *
     * @return The goodbye type.
     */
    public int getGoodbyeType() {
        return goodbyeType;
    }
    /**
     * Sets the goodbye type associated with this message body, its value can be 0, 1 or 2.
     *
     * @param goodbyeType The goodbye type.
     */
    public void setGoodbyeType(int goodbyeType) {
        this.goodbyeType = goodbyeType;
    }
}
