package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.exceptions.EmptyShelfException;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Player handles, at the end of a player's turn, the calculation of his new score
 */
public class Player implements Serializable {
    private final String nickname;
    private boolean connected;
    private int playerScore;
    private BoardFactory board;
    private EndGameToken endGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringTokenList = new ArrayList<>(2);
    private List<CommonTargetCard> commonTargetCardList;
    private PersonalTargetCard personalTargetCard;

    /**
     * Constrcutor for Player makes sure the connected attribute is set to true, as the player is connecting right now
     * @param nickname is passed by the Player during the connection
     */
    public Player(String nickname) {
        this.nickname = nickname;
        endGameToken = null;
        playerScore=0;
        this.connected = true;
    }

    /**
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the player's shelf
     */
    public Shelf getShelf() {
        return shelf;
    }

    /**
     * handles the operation to update the score a Player
     * after the server receives and executes on the model a move from the active player at the moment.
     * It doesn't pass it back as an argument, it updates the Player's attribute "playerScore"
     */
    public void updateScore() {
        try {
            playerScore = 0;
            if (hasEndGameToken()) {
                playerScore += endGameToken.getValue();
            }
            playerScore += personalTargetCard.calculatePoints(shelf);

            try {
                playerScore += shelf.calculateAdjacentItemsPoints();
            }
            catch (EmptyShelfException ignored) {
            }
            finally {
                for(CommonTargetCard commonTargetCard:commonTargetCardList){
                    if (commonTargetCard.check(shelf)) {
                        ScoringToken scoringToken=commonTargetCard.assignToken(this); //assignToken verifies the player doesn't already have obtained a token from that same commonTargetCard
                        if(scoringToken!=null){
                            scoringTokenList.add(scoringToken);
                        }
                    }
                }
                for (ScoringToken token : scoringTokenList) {
                    if (token != null) {
                        playerScore += token.getValue();
                    }
                }
            }
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * when a Player is awarded a EndGameToken, it is set as his attribute and this method also occupies of setting the player's
     * nickname in the takenBy attribute of EndGameToken
     * @param endGameToken
     */
    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
        EndGameToken.getEndGameToken().setTakenBy(nickname);
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
        if( scoringToken != null ) {
            this.scoringTokenList.add(scoringToken);
            scoringToken.setWhoItWasTakenBy(nickname);
        }
    }

    public ScoringToken getScoringToken(int i){
        return scoringTokenList.get(i);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public PersonalTargetCard getPersonalTargetCard() {
        return personalTargetCard;
    }

    public boolean checkColumnChosen(int numberOfItemsPicked, int column) {
        return shelf.checkColumn(numberOfItemsPicked,column);
    }
    public List<CommonTargetCard> getCommonTargetCardList() {
        return commonTargetCardList;
    }

    public BoardFactory getBoard() {
        return board;
    }

    public List<ScoringToken> getScoringTokenList() {
        return scoringTokenList;
    }

    public boolean hasEndGameToken(){
        return endGameToken != null;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setScoringTokenList(List<ScoringToken> scoringTokenList) {
        this.scoringTokenList = scoringTokenList;
    }
}
