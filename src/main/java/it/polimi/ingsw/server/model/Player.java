package it.polimi.ingsw.server.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private int playerScore;
    private BoardFactory board;
    private EndGameToken endGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringTokenList = new ArrayList<>(2);
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
    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }

    public void setBoard(BoardFactory board) {
        this.board = board;
    }

    public void setCommonTargetCardList(List<CommonTargetCard> commonTargetCardList) {
        this.commonTargetCardList = commonTargetCardList;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public void setPersonalTargetCard(PersonalTargetCard personalTargetCard) {
        this.personalTargetCard = personalTargetCard;
    }

    public void addScoringToken(ScoringToken scoringToken) {
        if( scoringToken != null )
            this.scoringTokenList.add(scoringToken);
    }

    public ScoringToken getScoringToken(int i){
        return scoringTokenList.get(i);
    }


}
