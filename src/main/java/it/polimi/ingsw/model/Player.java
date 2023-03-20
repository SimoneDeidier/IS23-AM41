package it.polimi.ingsw.model;

import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private int playerScore;
    private boolean EndGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringToken;
    private List<CommonTargetCard> commonTargetCard;
    private PersonalTargetCard personalTargetCard;

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

    public boolean isEndGameToken() {
        return EndGameToken;
    }

    public void setEndGameToken(boolean endGameToken) {
        EndGameToken = endGameToken;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public List<ScoringToken> getScoringToken() {
        return scoringToken;
    }

    public void setScoringToken(List<ScoringToken> scoringToken) {
        this.scoringToken = scoringToken;
    }

    public List<CommonTargetCard> getCommonTargetCard() {
        return commonTargetCard;
    }

    public void setCommonTargetCard(List<CommonTargetCard> commonTargetCard) {
        this.commonTargetCard = commonTargetCard;
    }

    public PersonalTargetCard getPersonalTargetCard() {
        return personalTargetCard;
    }

    public void setPersonalTargetCard(PersonalTargetCard personalTargetCard) {
        this.personalTargetCard = personalTargetCard;
    }
}
