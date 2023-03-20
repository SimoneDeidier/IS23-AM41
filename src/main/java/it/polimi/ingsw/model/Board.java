package it.polimi.ingsw.model;

import java.util.List;

public class Board {

    private Board instance = null;
    private ItemsBag itemsBag;
    private List<List<Item>> boardMatrix;

    private Board(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
    }

    public Board getBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new Board(itemsBag);
        }
        return instance;
    }

    public void refillBoard() {

    }

    public Item pickItem(int x, int y) {
        // todo da implementare
        return null;
    }

}
