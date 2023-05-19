package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import it.polimi.ingsw.server.model.Shelf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class CommonTargetCard implements Serializable {


    protected final static int ROWS = 6;
    protected final static int COLUMNS = 5;
    protected final static int COLORS = 6;
    protected List<ScoringToken> scoringTokensList;

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


    public abstract boolean check(Shelf shelf);

    public ScoringToken assignToken(Player player){
        for(ScoringToken token: scoringTokensList){
            if(token.isTakeable()){
                token.setOwner(player);
                return token;
            }
            else if(token.getOwner().equals(player)){
                break;
            }
        }
        return null;
    }




}
