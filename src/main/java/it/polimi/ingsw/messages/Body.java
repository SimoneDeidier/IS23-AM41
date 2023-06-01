package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.PersonalTargetCard;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public String getPlayerNickname() {
        return playerNickname;
    }

    public List<int[]> getPositionsPicked() {
        return positionsPicked;
    }

    public int getColumn() {
        return column;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isOnlyOneCommon() {
        return onlyOneCommon;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void setPositionsPicked(List<int[]> positionsPicked) {
        this.positionsPicked = positionsPicked;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setOnlyOneCommon(boolean onlyOneCommon) {
        this.onlyOneCommon = onlyOneCommon;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public String getText() {
        return text;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public int getPersonalCardNumber() {
        return personalCardNumber;
    }

    public void setPersonalCardNumber(int personalCardNumber) {
        this.personalCardNumber = personalCardNumber;
    }

    public NewView getNewView() {
        return newView;
    }

    public void setNewView(NewView newView) {
        this.newView = newView;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public List<String> getCommonTargetCardsName() {
        return commonTargetCardsName;
    }

    public int getGoodbyeType() {
        return goodbyeType;
    }

    public void setGoodbyeType(int goodbyeType) {
        this.goodbyeType = goodbyeType;
    }
}
