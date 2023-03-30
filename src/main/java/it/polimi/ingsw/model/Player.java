package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private boolean connected;
    private int playerScore;
    private EndGameToken endGameToken;
    private Shelf shelf;
    private ArrayList<ScoringToken> scoringToken;
    private ArrayList<CommonTargetCard> commonTargetCard;
    private PersonalTargetCard personalTargetCard;

    public Player(String nickname) {
        this.nickname = nickname;
        endGameToken = null;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public EndGameToken getEndGameToken() {
        return endGameToken;
    }

    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public ArrayList<ScoringToken> getScoringToken() {
        return scoringToken;
    }

    public void setScoringToken(ArrayList<ScoringToken> scoringToken) {
        this.scoringToken = scoringToken;
    }

    public ArrayList<CommonTargetCard> getCommonTargetCard() {
        return commonTargetCard;
    }

    public void setCommonTargetCard(ArrayList<CommonTargetCard> commonTargetCard) {
        this.commonTargetCard = commonTargetCard;
    }

    public PersonalTargetCard getPersonalTargetCard() {
        return personalTargetCard;
    }

    public void setPersonalTargetCard(PersonalTargetCard personalTargetCard) {
        this.personalTargetCard = personalTargetCard;
    }

    public void updateScore() {
        playerScore = 0;
        if(endGameToken != null) {
            playerScore += endGameToken.getValue();
        }
        for(ScoringToken token : scoringToken) {
            if(token != null) {
                playerScore += token.getValue();
            }
        }
        playerScore += personalTargetCard.calculatePoints(shelf);
        playerScore += shelf.calculateAdjacentItemsPoints();
    }
}
