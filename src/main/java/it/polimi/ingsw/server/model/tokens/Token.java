package it.polimi.ingsw.server.model.tokens;

import it.polimi.ingsw.server.model.Player;

public abstract class Token {
    protected int value;
    protected Player owner = null;

    public boolean isTakeable(){
        return owner == null;
    }
    public int getValue() {
        return value;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        if(owner != null) {
            owner.addScoringToken(this);
        }
    }
}
