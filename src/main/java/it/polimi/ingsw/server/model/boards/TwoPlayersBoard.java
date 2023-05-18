package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;

import java.io.Serializable;

public class TwoPlayersBoard extends BoardFactory implements Serializable {

    private final static int ROWS = 9;
    private final static int COLUMNS = 9;
    private static BoardFactory instance;

    private TwoPlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.resetItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
        boardMatrix = new Item[ROWS][COLUMNS];
    }

    public static BoardFactory getTwoPlayersBoard() {
        if(instance == null) {
            instance = new TwoPlayersBoard();
        }
        return instance;
    }

}
