package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;

import java.io.Serializable;

public class ThreePlayersBoard extends BoardFactory {

    public ThreePlayersBoard() {
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

    @Override
    public boolean[][] createBitMask() {
        boolean[][] bitMask = super.createBitMask();
        bitMask[0][3] = true;
        bitMask[2][2] = true;
        bitMask[2][6] = true;
        bitMask[3][8] = true;
        bitMask[5][0] = true;
        bitMask[6][2] = true;
        bitMask[6][6] = true;
        bitMask[8][5] = true;

        return bitMask;
    }
}
