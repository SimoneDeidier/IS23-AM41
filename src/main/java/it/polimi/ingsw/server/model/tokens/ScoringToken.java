package it.polimi.ingsw.server.model.tokens;

import java.io.Serializable;

public class ScoringToken implements Serializable {
    private final int value;
    private String takenBy;

    /**
     * Constructor of the ScoringToken
     *
     * @param value value of the scoring token
     */
    public ScoringToken(int value) {
        this.value=value;
        this.takenBy=null;
    }

    /**
     * Return true if the scoring token is takeable
     *
     * @return true if the scoring token is takeable
     */
    public boolean isTakeable(){
        return takenBy==null;
    }

    /**
     * Return the value of the token
     *
     * @return the value of the token
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the nickname of the user that took the token
     *
     * @param nickname of the user that took the token
     */
    public void setWhoItWasTakenBy(String nickname) {
        this.takenBy = nickname;
    }

    /**
     * Return the nickname of the user that took the token
     * @return the nickname of the user that took the token
     */
    public String whoTookThatToken() {
        return takenBy;
    }
}
