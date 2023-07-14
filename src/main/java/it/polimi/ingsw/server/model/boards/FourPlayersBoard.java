package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemsBag;

import java.io.Serializable;

/**
 * Represents a Board, it has a bitmask specific for when the maxPlayerNumber is four
 */
public class FourPlayersBoard extends BoardFactory {

    /**
     * Creates the board, resetting the itemsBag since it's a singleton, inherited from a previous match played on the server
     */
    public FourPlayersBoard() {
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

    /**
     * Along with the TwoPlayerBoard's bitMask created by super.createBitMask(),
     * it adds valid slots for when the maxPlayerNumber is four
     * @return a bitMask valid during four players' matches
     */
    @Override
    public boolean[][] createBitMask() {
        boolean[][] bitMask = super.createBitMask();
        bitMask[0][3] = true;
        bitMask[0][4] = true;
        bitMask[1][5] = true;
        bitMask[2][2] = true;
        bitMask[2][6] = true;
        bitMask[3][1] = true;
        bitMask[3][8] = true;
        bitMask[4][0] = true;
        bitMask[4][8] = true;
        bitMask[5][0] = true;
        bitMask[6][2] = true;
        bitMask[6][6] = true;
        bitMask[5][7] = true;
        bitMask[7][3] = true;
        bitMask[8][4] = true;
        bitMask[8][5] = true;


        return bitMask;
    }

}
