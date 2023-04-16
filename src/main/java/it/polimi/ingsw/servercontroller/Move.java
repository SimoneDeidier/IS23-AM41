package it.polimi.ingsw.servercontroller;

import java.util.List;

public class Move {
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
}
