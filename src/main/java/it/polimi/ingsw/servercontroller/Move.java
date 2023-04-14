package it.polimi.ingsw.servercontroller;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class Move {
    private Player player;

    private List<int[]> positionsPicked; //like (4 0) (4 1) (4 2), in order of insertion in the shelf
                                 //0 --> first to be inserted and so on
    private int column;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<int[]> getPositionsPicked() {
        return positionsPicked;
    }

    public void setPositionsPicked(List<int[]> positionsPicked) {
        this.positionsPicked = positionsPicked;
    }

    public int getColumn() {
        return column;
    }
}
