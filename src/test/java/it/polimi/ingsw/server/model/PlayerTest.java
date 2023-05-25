package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
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
}