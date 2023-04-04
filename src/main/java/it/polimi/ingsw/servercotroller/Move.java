package it.polimi.ingsw.servercotroller;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class Move {
    Player player;

    List<int[]> positionsPicked; //like (4 0) (4 1) (4 2)

    int column;

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
}
