package it.polimi.ingsw.model;

public class FourPlayersBoard extends Board {

    private static Board instance;

    private FourPlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
        refillBoard();
    }

    public static Board getFourPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new FourPlayersBoard(itemsBag);
        }
        return instance;
    }

    @Override
    public int[][] createBitMask() {
        int[][] bitMask = super.createBitMask();
        for(int i = 3; i < 5; i++) {
            bitMask[0][i] = 1;
            bitMask[8][i+1] = 1;
        }
        bitMask[0][4] = 1;
        bitMask[1][5] = 1;
        bitMask[3][1] = 1;
        bitMask[4][0] = 1;
        bitMask[4][8] = 1;
        bitMask[5][7] = 1;
        bitMask[7][3] = 1;
        bitMask[8][4] = 1;


        return bitMask;
    }

}
