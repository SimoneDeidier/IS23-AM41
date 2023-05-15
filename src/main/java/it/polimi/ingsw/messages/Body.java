package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;

import java.net.Socket;
import java.util.List;

public class Body {
    private String playerNickname;
    private List<int[]> positionsPicked;    //like (4 0) (4 1) (4 2), in order of insertion in the shelf
    private int column;                     //0 --> first to be inserted and so on
    private int numberOfPlayers;
    private boolean onlyOneCommon;
    private String receiverNickname;
    private String senderNickname;
    private String text;
    private PersonalTargetCard personalCard;
    private List<Player> view;

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

    public PersonalTargetCard getPersonalCard() {
        return personalCard;
    }

    public void setPersonalCard(PersonalTargetCard personalCard) {
        this.personalCard = personalCard;
    }

    public List<Player> getView() {
        return view;
    }

    public void setView(List<Player> view) {
        this.view = view;
    }
}
