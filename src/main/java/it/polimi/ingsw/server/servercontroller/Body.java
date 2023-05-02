package it.polimi.ingsw.server.servercontroller;

import java.util.List;

public class Body {
    private String playerNickname;
    private List<int[]> positionsPicked;    //like (4 0) (4 1) (4 2), in order of insertion in the shelf
    private int column;                     //0 --> first to be inserted and so on

    public String getPlayerNickname() {
        return playerNickname;
    }

    public List<int[]> getPositionsPicked() {
        return positionsPicked;
    }

    public int getColumn() {
        return column;
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
}
