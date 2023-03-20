package it.polimi.ingsw.model;

public class EndGameToken extends Token{
    private EndGameToken instance;

    private EndGameToken();

    public getEndGameToken(){
        if(instance==null)
            instance = new EndGameToken();
        return instance;
    }

}
