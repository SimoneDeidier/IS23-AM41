package it.polimi.ingsw.server.model.tokens;

public class EndGameToken extends Token{

    private static EndGameToken instance;

    private EndGameToken(int value) {
        this.value = value;
    }

    public static EndGameToken getEndGameToken(){
        if(instance==null)
            instance = new EndGameToken(1);
        return instance;
    }

    public void resetEndGameToken(){
        setOwner(null);
    }

}