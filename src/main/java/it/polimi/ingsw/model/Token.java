package it.polimi.ingsw.model;

public abstract class Token {
    private int value;
    private Player owner;

    public boolean isTakeable(){ //Not abstract because it's the same
        if owner==null           //for both Scoring and EndGame tokens
            return true;
        return false;
    }

    public int getValue() {
        return value;
    }

    public Player getOwner() {
        return owner;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
