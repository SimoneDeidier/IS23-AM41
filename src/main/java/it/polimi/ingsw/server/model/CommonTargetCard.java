package it.polimi.ingsw.server.model;

import java.util.*;

public abstract class CommonTargetCard {


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
