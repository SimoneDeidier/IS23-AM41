package it.polimi.ingsw.server.model.tokens;

import java.io.Serializable;

public class ScoringToken implements Serializable {
    private final int value;
    private String takenBy;
    public ScoringToken(int value) {
        this.value=value;
        this.takenBy=null;
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
