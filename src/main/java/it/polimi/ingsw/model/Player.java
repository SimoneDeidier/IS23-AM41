package it.polimi.ingsw.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private int playerScore;
    private EndGameToken endGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringTokenList;
    private List<CommonTargetCard> commonTargetCardList;
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
        try {
            playerScore = 0;
            if (endGameToken != null) {
                playerScore += endGameToken.getValue();
            }
            //Assegnare token
            for (ScoringToken token : scoringTokenList) {
                if (token != null) {
                    playerScore += token.getValue();
                }
            }
            playerScore += personalTargetCard.calculatePoints(shelf, 0);
            playerScore += shelf.calculateAdjacentItemsPoints();
        }
        catch (EmptyShelfException e) {
            System.out.println("EMPTY SHELF EXCEPTION");
            System.out.println("Player: " + nickname);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCommonTargetCard(CommonTargetCard card){
        commonTargetCardList.add(card);
    }

    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }
}
