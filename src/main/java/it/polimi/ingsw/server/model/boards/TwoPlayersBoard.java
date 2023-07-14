package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;

import java.io.Serializable;

/**
 * Represents a Board, it has a bitmask specific for when the maxPlayerNumber is two
 */
public class TwoPlayersBoard extends BoardFactory {

    private final static int ROWS = 9;
    private final static int COLUMNS = 9;

    /**
     * Creates the board, resetting the itemsBag since it's a singleton, inherited from a previous match played on the server
     */
    public TwoPlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.resetItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
        boardMatrix = new Item[ROWS][COLUMNS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLUMNS;j++){
                boardMatrix[i][j]=null;
            }
        }
    }

}
