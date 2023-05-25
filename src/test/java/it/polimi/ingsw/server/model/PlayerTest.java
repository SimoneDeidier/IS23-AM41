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
        scoringToken.setOwner(player);
        player.addScoringToken(scoringToken);
        player.setPersonalTargetCard(new PersonalTargetCard(0));
        List<CommonTargetCard> commonTargetList = new ArrayList<>();
        commonTargetList.add(new CommonTwoColumns(2));
        player.setCommonTargetCardList(commonTargetList);
    }

    @Test
    void getNickname() {
        assertNull(emptyPlayer.getNickname());
        assertEquals(PLAYER_NAME, player.getNickname());
    }

    @Test
    void getShelf() {
        assertNull(emptyPlayer.getShelf());
        assertNotNull(player.getShelf());
    }

    @Test
    void updateScore() {
        player.updateScore();
        assertEquals(9, player.getPlayerScore());
    }
    @Test
    void isConnected() {
        assertTrue(player.isConnected());
    }

    @Test
    void addScoringToken(){
        ScoringToken scoringToken = new ScoringToken(8);
        Player player1 = new Player("player1");
        player1.addScoringToken(scoringToken);
        assertEquals(scoringToken, player1.getScoringToken(0));
    }


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

    @Test
    void setPersonalTargetCard() throws IOException, URISyntaxException {
        PersonalTargetCard personalTargetCard = new PersonalTargetCard(0);
        player.setPersonalTargetCard(personalTargetCard);
        assertEquals(personalTargetCard, player.getPersonalTargetCard());
    }
    @Test
    void getPersonalTargetCard() throws IOException, URISyntaxException {
        PersonalTargetCard personalTargetCard = new PersonalTargetCard(0);
        player.setPersonalTargetCard(personalTargetCard);
        assertEquals(personalTargetCard, player.getPersonalTargetCard());
    }


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


    @Test
    void getBoard(){
        TwoPlayersBoard board = new TwoPlayersBoard();
        player.setBoard(board);
        assertEquals(board, player.getBoard() );

    }


}