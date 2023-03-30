package it.polimi.ingsw;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class App {

    private final static int ROWS = 6;
    private final static int COLUMNS = 5;

    public static void main(String[] args) throws NotEnoughSpaceInColumnException, InvalidBoardPositionException, NullItemPickedException {
        Shelf shelf = new Shelf();
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        BoardFactory board = TwoPlayersBoardFactory.getTwoPlayersBoard(itemsBag);

        itemsBag.setupBag();
        board.refillBoard();
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                List<Item> itemList = new ArrayList<>();
                Item item = itemsBag.pickItem();
                itemList.add(item);
                shelf.insertItems(j, itemList);
                String color = item.getColor().toString();
                System.out.print("| " + color.charAt(0) + " ");
            }
            System.out.println("|");
        }
        System.out.println("\n\nCALCOLO PUNTEGGIO ADIACENZE");
        System.out.println("Punti: " + shelf.calculateAdjacentItemsPoints());

       Item item = board.pickItem(5, 4);
       System.out.println(item);
       Item item2 = board.pickItem(5, 4);
       System.out.println(item2);
    }
}
