package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.EmptyShelfException;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.servercontroller.controllerstates.RunningGameState;
import it.polimi.ingsw.server.servercontroller.controllerstates.ServerInitState;
import it.polimi.ingsw.server.servercontroller.controllerstates.WaitingForPlayerState;
import it.polimi.ingsw.server.servercontroller.controllerstates.WaitingForSavedGameState;
import it.polimi.ingsw.server.servercontroller.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;


    @BeforeEach
    void initialize() {
        controller = GameController.getGameController(new Server());
        controller.prepareForNewGame();
        controller.setState(new WaitingForPlayerState());
        controller.setupLobbyParameters(0, true);
    }


    @Test
    void getAvailableSlot() {
        //WaitingForPlayerState
        assertEquals(-2, controller.getAvailableSlot());
        controller.setupLobbyParameters(4, true);
        assertEquals(4, controller.getAvailableSlot());
        controller.addPlayer(new Player("player1"));
        assertEquals(3, controller.getAvailableSlot());
        controller.addPlayer(new Player("player2"));
        assertEquals(2, controller.getAvailableSlot());
        controller.addPlayer(new Player("player3"));
        assertEquals(1, controller.getAvailableSlot());
        controller.addPlayer(new Player("player4"));
        assertEquals(0, controller.getAvailableSlot());
        //ServerInitState
        controller.setState(new ServerInitState());
        assertEquals(-1, controller.getAvailableSlot());
        //RunningGameState
        controller.setState(new RunningGameState());
        assertEquals(0, controller.getAvailableSlot());
        //WaitingForSavedGameState
        controller.setState(new WaitingForSavedGameState());
        assertEquals(0, controller.getAvailableSlot());
        controller.getPlayerList().get(0).setConnected(false);
        assertEquals(1, controller.getAvailableSlot());
        controller.getPlayerList().get(1).setConnected(false);
        assertEquals(2, controller.getAvailableSlot());
        controller.getPlayerList().get(2).setConnected(false);
        assertEquals(3, controller.getAvailableSlot());
        controller.getPlayerList().get(3).setConnected(false);
        assertEquals(4, controller.getAvailableSlot());
    }


    @Test
    void isGameReady() {
        //WaitingForPlayerState
        controller.setupLobbyParameters(3, true);
        assertFalse(controller.isGameReady());
        controller.addPlayer(new Player("player0"));
        assertFalse(controller.isGameReady());
        controller.addPlayer(new Player("player1"));
        assertFalse(controller.isGameReady());
        controller.addPlayer(new Player("player2"));
        assertTrue(controller.isGameReady());
        //WaitingForSavedGameState
        controller.setState(new WaitingForSavedGameState());
        for (int i = 0; i < 3; i++)
            controller.getPlayerList().get(i).setConnected(false);
        assertFalse(controller.isGameReady());
        for (int i = 0; i < 3; i++)
            controller.getPlayerList().get(i).setConnected(true);
        assertTrue(controller.isGameReady());
    }

    @Test
    void checkNicknameAvailability() {
        //WaitingForPlayerState
        assertEquals(1, controller.checkNicknameAvailability("player1"));
        controller.addPlayer(new Player("player0"));
        assertEquals(1, controller.checkNicknameAvailability("player1"));
        controller.addPlayer(new Player("player1"));
        assertEquals(0, controller.checkNicknameAvailability("player1"));
        //WaitingForSavedGameState
        controller.setState(new WaitingForSavedGameState());
        controller.getPlayerList().get(0).setConnected(false);
        assertEquals(1, controller.checkNicknameAvailability("player0"));
        assertEquals(-1, controller.checkNicknameAvailability("player1"));
        assertEquals(-1, controller.checkNicknameAvailability("player2"));
        //ServerInitState
        controller.setState(new ServerInitState());
        assertEquals(0, controller.checkNicknameAvailability("player0"));
        assertEquals(0, controller.checkNicknameAvailability("player1"));
        assertEquals(0, controller.checkNicknameAvailability("player2"));
        //RunningGameState
        controller.setState(new ServerInitState());
        assertEquals(0, controller.checkNicknameAvailability("player0"));
        assertEquals(0, controller.checkNicknameAvailability("player1"));
        assertEquals(0, controller.checkNicknameAvailability("player2"));
    }


    @Test
    void getListItems() {
        Body body = new Body();
        List<int[]> list = new ArrayList<int[]>();
        int[] p = new int[2];
        p[0] = 5;
        p[1] = 5;
        list.add(p);
        int[] p2 = new int[2];
        p2[0] = 6;
        p2[1] = 6;
        list.add(p2);
        body.setPositionsPicked(list);
        BoardFactory board = new TwoPlayersBoard();
        board.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 5);
        board.setBoardMatrixElement(new Item(ItemColor.YELLOW), 6, 6);
        controller.setBoard(board);
        ItemColor itColor = controller.getListItems(body).get(0).getColor();
        assertEquals(ItemColor.YELLOW, itColor);
        assertNull(board.getBoardMatrixElement(5, 5));
    }


    @Test
    void addPlayer() {
        //WaintingForPlayerState
        controller.addPlayer(new Player("player0"));
        controller.addPlayer(new Player("player1"));
        controller.addPlayer(new Player("player2"));
        assertEquals(3, controller.getPlayerList().size());
        assertEquals("player0", controller.getPlayerList().get(0).getNickname());
        assertEquals("player1", controller.getPlayerList().get(1).getNickname());
        assertEquals("player2", controller.getPlayerList().get(2).getNickname());
        //WaitingForSavedGameState
        controller.setState(new WaitingForSavedGameState());
        controller.getPlayerList().get(0).setConnected(false);
        controller.addPlayer(new Player("player0"));
        controller.getPlayerList().get(2).setConnected(false);
        assertFalse(controller.getPlayerList().get(2).isConnected());
        controller.addPlayer(new Player("player2"));
        for (int i = 0; i < 3; i++) {
            assertTrue(controller.getPlayerList().get(i).isConnected());
        }
        controller.getPlayerList().get(0).setConnected(false);
        controller.addPlayer(new Player("player1"));
        assertFalse(controller.getPlayerList().get(0).isConnected());
    }


    @Test
    void nextIndexCalc() throws NotEnoughSpaceInColumnException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        controller.changeState(new WaitingForPlayerState());
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        assertEquals(0, controller.nextIndexCalc(1));
        assertEquals(1, controller.nextIndexCalc(0));
        controller.setLastTurn(true);
        assertEquals(-1, controller.nextIndexCalc(1));
    }


    @Test
    void checkLastTurn() throws NotEnoughSpaceInColumnException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        assertEquals(2, controller.getPlayerList().size());
        Shelf shelf1 = new Shelf();
        Shelf shelf2 = new Shelf();

        //partially filled shelf for player1
        shelf1.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelf1.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        player1.setShelf(shelf1);
        player2.setShelf(shelf2);
        assertFalse(controller.checkLastTurn());

        //full shelf for player 2
        shelf2.insertItems(0, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE)));
        shelf2.insertItems(1, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE)));
        shelf2.insertItems(2, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.PINK), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.PINK)));
        shelf2.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.YELLOW)));
        shelf2.insertItems(4, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
        player2.setShelf(shelf2);
        assertTrue(controller.checkLastTurn());

    }

    @Test
    void checkMove() throws NotEnoughSpaceInColumnException {
        Body body = new Body();
        body.setPlayerNickname("player2");
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        BoardFactory board1 = new TwoPlayersBoard();
        controller.setBoard(board1);
        //check active player
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.setActivePlayer(player1);
        assertFalse(controller.checkMove(body));
        controller.setActivePlayer(player2);
        //check board.checkMove
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 3);
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 4);
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 5);
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 6, 5);
        int[] array1 = {5, 3};
        int[] array2 = {5, 4};
        int[] array3 = {5, 5};
        int[] array4 = {6, 5};
        //items not in line
        List<int[]> positionsPicked2 = new ArrayList<>();
        positionsPicked2.add(array2);
        positionsPicked2.add(array3);
        positionsPicked2.add(array4);
        body.setPositionsPicked(positionsPicked2);
        player2.setShelf(new Shelf());
        assertFalse(controller.checkMove(body));
        //items in line
        List<int[]> positionsPicked = new ArrayList<>();
        positionsPicked.add(array1);
        positionsPicked.add(array2);
        positionsPicked.add(array3);
        body.setPositionsPicked(positionsPicked);
        player2.setShelf(new Shelf());
        assertTrue(controller.checkMove(body));
        //enough space in column
        body.setColumn(0);
        Shelf shelf = new Shelf();
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(ItemColor.YELLOW));
        itemList.add(new Item(ItemColor.YELLOW));
        shelf.insertItems(0, itemList);
        player2.setShelf(shelf);
        assertTrue(controller.checkMove(body));
        //not enough space in column
        shelf.insertItems(0, itemList);
        assertFalse(controller.checkMove(body));
    }

    @Test
    void checkBoardNeedForRefill() {
        BoardFactory board1 = new TwoPlayersBoard();
        controller.setBoard(board1);


        //item along the perimeter
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 1, 3);
        assertTrue(controller.checkBoardNeedForRefill());
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 2, 5);
        assertTrue(controller.checkBoardNeedForRefill());
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 3, 6);
        assertTrue(controller.checkBoardNeedForRefill());
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 4, 7);
        assertTrue(controller.checkBoardNeedForRefill());
        //item in medias res
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 5);
        assertTrue(controller.checkBoardNeedForRefill());
        board1.setBoardMatrixElement(new Item(ItemColor.YELLOW), 5, 6);
        assertFalse(controller.checkBoardNeedForRefill());

    }

    @Test
    void testPresentation() throws FullLobbyException, WaitForLobbyParametersException, GameStartException, CancelGameException, FirstPlayerException {
        controller.setState(new ServerInitState());
        //assertThrows(FirstPlayerException.class, () -> controller.presentation("DHSahDusahuiH"));
        //assertThrows(WaitForLobbyParametersException.class, () -> controller.presentation("Marco"));
        controller.setState(new WaitingForPlayerState());
        controller.setMaxPlayerNumber(3);
        assertEquals(1, controller.presentation("Marco"));
        assertEquals(0, controller.presentation("Marco"));
        //assertThrows(GameStartException.class, () -> controller.presentation("Davide"));
        //assertThrows(FullLobbyException.class, () -> controller.presentation("Mirko"));
    }

    @Test
    void setupGame() {
        Player player0 = new Player("player0");
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        controller.addPlayer(player0);
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.addPlayer(player3);

        controller.setupLobbyParameters(4, false);
        controller.setupGame(false);
        //check setupCommonTargetList
        assertEquals(2, controller.getCommonTargetCardsList().size());
        assertNotSame(controller.getCommonTargetCardsList().get(0), controller.getCommonTargetCardsList().get(1));
        //check setupBoard
        assertInstanceOf(FourPlayersBoard.class, controller.getBoard());
        //check refill
        assertFalse(controller.checkBoardNeedForRefill());
        //check setupPlayers
        assertEquals(controller.getBoard(), player0.getBoard());
        assertInstanceOf(Shelf.class, player1.getShelf());
        assertEquals(player2.getCommonTargetCardList(), controller.getCommonTargetCardsList());
    }
}
