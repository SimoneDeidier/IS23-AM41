package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
* Test class for the player class
*/
class PlayerTest {

    private Player emptyPlayer;
    private Player player;
    private final static String PLAYER_NAME = "TestPlayer";

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        emptyPlayer = new Player(null);
        emptyPlayer.setCommonTargetCardList(null);
        emptyPlayer.setPersonalTargetCard(null);
        player = new Player(PLAYER_NAME);
        player.setShelf(new Shelf());
        player.setConnected(true);
        player.setBoard(new TwoPlayersBoard());
        player.setEndGameToken(EndGameToken.getEndGameToken());
        ScoringToken scoringToken = new ScoringToken(8);
        player.addScoringToken(scoringToken);
        player.setPersonalTargetCard(new PersonalTargetCard(0));
        List<CommonTargetCard> commonTargetList = new ArrayList<>();
        commonTargetList.add(new CommonTwoColumns(2));
        player.setCommonTargetCardList(commonTargetList);
    }

/**
* Check the behaviour of getNickname
*/
    @Test
    void getNickname() {
        assertNull(emptyPlayer.getNickname());
        assertEquals(PLAYER_NAME, player.getNickname());
    }

/**
* Check if getshelf return null or not null on an null and not null shelf
*/
    @Test
    void getShelf() {
        assertNull(emptyPlayer.getShelf());
        assertNotNull(player.getShelf());
    }

/**
* Checks if the score uodates as expected using the method UpdateScore
*/
    @Test
    void updateScore() {
        player.updateScore();
        assertEquals(9, player.getPlayerScore());
    }

/**
* Checks if the isConnected method return true on a connected player
*/
    @Test
    void isConnected() {
        assertTrue(player.isConnected());
    }

/**
* Checks if adding a token to a player actually add it
*/
    @Test
    void addScoringToken(){
        ScoringToken scoringToken = new ScoringToken(8);
        Player player1 = new Player("player1");
        player1.addScoringToken(scoringToken);
        assertEquals(scoringToken, player1.getScoringToken(0));
    }

/**
* Check if the checkColumnChosen behaves ad expected on a filled shelf
*/
    @Test
    void checkColumnChosen() throws NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();
        List<Item> list = new ArrayList<>();
        list.add(new Item(ItemColor.YELLOW));
        list.add(new Item(ItemColor.YELLOW));
        list.add(new Item(ItemColor.YELLOW));
        list.add(new Item(ItemColor.YELLOW));
        shelf.insertItems(0, list);
        player.setShelf(shelf);
        assertFalse(player.checkColumnChosen(3, 0));
        assertTrue(player.checkColumnChosen(3, 1));
    }

/**
* Check if the setPersonalTargetCard method actually sets the personal target card
*/
    @Test
    void setPersonalTargetCard() throws IOException, URISyntaxException {
        PersonalTargetCard personalTargetCard = new PersonalTargetCard(0);
        player.setPersonalTargetCard(personalTargetCard);
        assertEquals(personalTargetCard, player.getPersonalTargetCard());
    }

/**
* Checks if the getPersobalTargetCard method returns the correct personal
*/
    @Test
    void getPersonalTargetCard() throws IOException, URISyntaxException {
        PersonalTargetCard personalTargetCard = new PersonalTargetCard(0);
        player.setPersonalTargetCard(personalTargetCard);
        assertEquals(personalTargetCard, player.getPersonalTargetCard());
    }

/**
* Checks if the common target cards are set correctly by the setComminTargetCard method
*/
    @Test
    void setCommonTargetCardList(){
        List<CommonTargetCard> list = new ArrayList<>();
        CommonTargetCard commonTargetCard0 = new CommonDiagonal(4);
        list.add(commonTargetCard0);
        CommonTargetCard commonTargetCard1 = new CommonDiagonal(4);
        list.add(commonTargetCard1);
        player.setCommonTargetCardList(list);
        assertEquals(commonTargetCard0, player.getCommonTargetCardList().get(0));
        assertEquals(commonTargetCard1, player.getCommonTargetCardList().get(1));
    }

/**
* Checks if the correct common target cards are returned by the getCommonTargetCardList
*/
    @Test
    void getCommonTargetCardList(){
        List<CommonTargetCard> list = new ArrayList<>();
        CommonTargetCard commonTargetCard0 = new CommonDiagonal(4);
        list.add(commonTargetCard0);
        CommonTargetCard commonTargetCard1 = new CommonDiagonal(4);
        list.add(commonTargetCard1);
        player.setCommonTargetCardList(list);
        assertEquals(commonTargetCard0, player.getCommonTargetCardList().get(0));
        assertEquals(commonTargetCard1, player.getCommonTargetCardList().get(1));
    }

/**
* Checks if the getBoard method return the board correcly
*/
    @Test
    void getBoard(){
        TwoPlayersBoard board = new TwoPlayersBoard();
        player.setBoard(board);
        assertEquals(board, player.getBoard() );

    }

/**
* Checks if the player score is set correctly by the setplayerscore method
*/
    @Test
    void setPlayerScore(){
        player.setPlayerScore(10);
        assertEquals(10, player.getPlayerScore());
    }


/**
* Checks if the list of scoring token is set correctly by the method setScoringTokenList
*/
    @Test
    void ScoringTokenList(){
        List<ScoringToken> list = new ArrayList<>();
        list.add(new ScoringToken(8));
        player.setScoringTokenList(list);
        assertEquals(list, player.getScoringTokenList());
    }

}