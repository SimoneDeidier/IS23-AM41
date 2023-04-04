package it.polimi.ingsw.model;

public class FourPlayersBoard extends BoardFactory {

    private static BoardFactory instance;

    private FourPlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
    }

    public static BoardFactory getFourPlayersBoard() {
        if(instance == null) {
            instance = new FourPlayersBoard();
        }
        return instance;
    }

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
