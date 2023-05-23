package it.polimi.ingsw.server.servercontroller;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.servercontroller.controllerstates.WaitingForPlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void initialize(){
        controller = GameController.getGameController(new Server());
        controller.prepareForNewGame();
    }

    @Test
    void nextIndexCalc() throws NotEnoughSpaceInColumnException {
        Player player1 = new Player("ingconti");
        Player player2 = new Player("margara");
        controller.changeState(new WaitingForPlayerState());
        assertEquals(0, controller.getPlayerList().size());
        controller.addPlayer(player1);
        assertEquals(1, controller.getPlayerList().size());
        controller.addPlayer(player2);
        assertEquals(2, controller.getPlayerList().size());
        assertEquals(0,controller.nextIndexCalc(1));
        assertEquals(1,controller.nextIndexCalc(0));

}


    @Test
    void checkLastTurn() throws NotEnoughSpaceInColumnException {
        Player player1 = new Player("ingconti");
        Player player2 = new Player("margara");
        controller.changeState(new WaitingForPlayerState());
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
        body.setPlayerNickname("margara");
        controller.setupGame(true);
        Player player1 = new Player("ingconti");
        Player player2 = new Player("margara");
        BoardFactory board1 = TwoPlayersBoard.getTwoPlayersBoard();
        controller.setBoard(board1);


        //check active player
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.setActivePlayer(player1);
        assertFalse(controller.checkMove(body));
        controller.setActivePlayer(player2);
        //assertTrue(controller.checkMove(body));

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
        BoardFactory board1 = TwoPlayersBoard.getTwoPlayersBoard();
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
}
