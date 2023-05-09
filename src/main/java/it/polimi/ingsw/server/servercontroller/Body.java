package it.polimi.ingsw.server.servercontroller;

import java.util.List;

public class Body {
    private String playerNickname;
    private List<int[]> positionsPicked;    //like (4 0) (4 1) (4 2), in order of insertion in the shelf
    private int column;                     //0 --> first to be inserted and so on
    private int players;
    private boolean twoCommon;
    private String receiverNickname;
    private String text;

    public String getPlayerNickname() {
        return playerNickname;
    }

    public List<int[]> getPositionsPicked() {
        return positionsPicked;
    }

    public int getColumn() {
        return column;
    }

    public int getPlayers() {
        return players;
    }

    public boolean isTwoCommon() {
        return twoCommon;
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

    public void setPlayers(int players) {
        this.players = players;
    }

    public void setTwoCommon(boolean twoCommon) {
        this.twoCommon = twoCommon;
    }
}
