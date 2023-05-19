package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.items.ItemsBag;

import java.io.Serializable;

public class ThreePlayersBoard extends BoardFactory {

    private static BoardFactory instance;

    private ThreePlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.resetItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
    }

    public static BoardFactory getThreePlayersBoard() {
        if(instance == null) {
            instance = new ThreePlayersBoard();
        }
        return instance;
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
