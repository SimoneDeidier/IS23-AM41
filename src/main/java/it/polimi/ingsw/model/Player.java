package it.polimi.ingsw.model;

import java.util.List;

public class Player {
    private String nickname;
    private  boolean connected;
    private int playerScore;
    private boolean EndGameToken;
    private Shelf shelf;
    private List<ScoringToken> scoringToken;
    private List<CommonTargetCard> commonTargetCard;
    private PersonalTargetCard personalTargetCard;

}
