package it.polimi.ingsw.server.model.tokens;

import java.io.Serializable;

public class EndGameToken implements Serializable {

    private static EndGameToken instance;
    private int value;
    private String takenBy;

    private EndGameToken(int value) {
        this.value = value;
    }
    public static EndGameToken getEndGameToken(){
        if(instance==null)
            instance = new EndGameToken(1);
        return instance;
    }
    public void resetEndGameToken(){
        setTakenBy(null);
    }
    public boolean isTakeable(){
        return takenBy==null;
    }
    public int getValue() {
        return value;
    }

    public void setTakenBy(String nickname) {
        this.takenBy = nickname;
    }

    public String getTakenBy() {
        return takenBy;
    }
}
