package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import it.polimi.ingsw.server.model.Shelf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that represents the abstract concept of a CommonTargetCard, providing methods for all the actual Common Cards
 */
public abstract class CommonTargetCard implements Serializable {


    protected final static int ROWS = 6;
    protected final static int COLUMNS = 5;
    protected final static int COLORS = 6;
    protected List<ScoringToken> scoringTokensList;
    protected String name;

    /**
     *Constructor CommonTargetCard creates a new CommonTargetCard instance.
     * @param numberOfPlayers is needed to allocate the correct number of ScoringTokens, that varies according to the number of players
     */
    public CommonTargetCard(int numberOfPlayers){

        scoringTokensList = new ArrayList<>(numberOfPlayers);
        scoringTokensList.add(new ScoringToken(8));
        scoringTokensList.add(new ScoringToken(4));
        switch (numberOfPlayers) {
            case 3 -> scoringTokensList.add(new ScoringToken(6));
            case 4 -> {
                scoringTokensList.add(new ScoringToken(6));
                scoringTokensList.add(new ScoringToken(2));
            }
        }
        //We already order the list having the first element with the highest value
        scoringTokensList.sort(Comparator.comparing(ScoringToken::getValue));
        Collections.reverse(scoringTokensList);

    }

    /**
     * Verifies whether a shelf of a Player satisfies the condition of a specific Common Card
     * @return true if the condition is satisfied, false otherwise
     */
    public abstract boolean check(Shelf shelf);

    /**
     * The function responsible for assigning the tokens to the Players who passes the check for a CommonTargetCard
     * @param player is provided in order to only assign one token of a CommonTargetCard to the same player
     * @return the higher ScoringToken available for the CommonTargetCard or null if the Player had previously obtained a ScoringToken from the same CommonTargetCard
     */
    public ScoringToken assignToken(Player player){
        for(ScoringToken token: scoringTokensList){
            if(token.isTakeable()){
                token.setWhoItWasTakenBy(player.getNickname());
                return token;
            }
            else if(token.whoTookThatToken().equals(player.getNickname())){
                break;
            }
        }
        return null;
    }

    /**
     * @return the name of the CommonTargetCard
     */
    public String getName() {
        return name;
    }

    /**
     * @return the scoringTokenList of the CommonTargetCard
     */
    public List<ScoringToken> getScoringTokensList() {
        return scoringTokensList;
    }

    /**
     * Useful for test purposes and for reloading a previously saved game
     * @param scoringTokensList is used to manually set a scoringTokenList for a CommonTargetCard
     */
    public void setScoringTokensList(List<ScoringToken> scoringTokensList) {
        this.scoringTokensList = scoringTokensList;
    }
}
