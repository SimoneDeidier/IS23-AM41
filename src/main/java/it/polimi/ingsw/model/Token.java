package it.polimi.ingsw.model;

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

    public void setValue(int value) {
        this.value = value;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
