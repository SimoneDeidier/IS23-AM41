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

    public void updateScore(Player player) {
        try {
            playerScore = 0;
            if (endGameToken != null) {
                playerScore += endGameToken.getValue();
            }
            playerScore += personalTargetCard.calculatePoints(shelf, 0);
            playerScore += shelf.calculateAdjacentItemsPoints();
            if(commonTargetCardList.get(0).check(shelf)){
                scoringTokenList.add(commonTargetCardList.get(0).assignToken(player));
            }
            if(commonTargetCardList.size()==2 && commonTargetCardList.get(1).check(shelf)){
                scoringTokenList.add(commonTargetCardList.get(1).assignToken(player));
            }
            for (ScoringToken token : scoringTokenList) {
                if (token != null) {
                    playerScore += token.getValue();
                }
            }
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
