package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private int playerScore;
    private EndGameToken endGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringToken;
    private List<CommonTargetCard> commonTargetCard;
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

    public Shelf getShelf() {
        return shelf;
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
