package it.polimi.ingsw.model;

public class EndGameToken extends Token{
    private EndGameToken instance;

    private EndGameToken(int value) {
        this.value = value;
    }

    public EndGameToken getEndGameToken(){
        if(instance==null)
            instance = new EndGameToken(1);
        return instance;
    }

}
