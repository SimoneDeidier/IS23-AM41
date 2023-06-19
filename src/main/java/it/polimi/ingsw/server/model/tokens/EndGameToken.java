package it.polimi.ingsw.server.model.tokens;

import java.io.Serializable;

/**
 * Class EndGameToken uses the design pattern Singleton, since it can only to be assigned to one player during a game
 */
public class EndGameToken implements Serializable {

    private static EndGameToken instance;
    private int value;
    private String takenBy;

    /**
     * The constructor is private due to the design pattern Singleton
     */
    private EndGameToken(int value) {
        this.value = value;
    }

    /**
     * getEndGameToken is a static method used instead of the Constructor, due to the design pattern Singleton
     * @return the current EndGameToken if it already exists or creates a new EndGameToken if it still wasn't created
     */
    public static EndGameToken getEndGameToken(){
        if(instance==null)
            instance = new EndGameToken(1);
        return instance;
    }

    /**
     * resets the EndGameToken in order to prepare it for a new game, it's due to the design pattern Singleton
     */
    public void resetEndGameToken(){
        setTakenBy(null);
    }

    /**
     * If no one has yet obtained the EndGameToken in this game, the EndGameToken is takeable
     * @return true if the EndGameToken is takeable, false otherwise
     */
    public boolean isTakeable(){
        return takenBy==null;
    }

    /**
     * @return the value the EndGameToken has, it's always only one point
     */
    public int getValue() {
        return value;
    }

    /**
     * The first player to complete his board gets the EndGameToken and his nickname is set as the EndGameToken's TakenBy attribute
     * @param nickname is the nickname of the player who is getting the EndGameToken
     */
    public void setTakenBy(String nickname) {
        this.takenBy = nickname;
    }

    /**
     * @return the nickname of the player who owns the EndGameToken or null if it still wasn't assigned
     */
    public String getTakenBy() {
        return takenBy;
    }
}
