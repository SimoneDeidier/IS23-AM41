package it.polimi.ingsw.model;

public class ThreePlayersBoard extends Board {

    private static Board instance;

    private ThreePlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
        refillBoard();
    }

    public static Board getThreePlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new ThreePlayersBoard(itemsBag);
        }
        return instance;
    }

    @Override
    public int[][] createBitMask() {
        int[][] bitMask = super.createBitMask();
        bitMask[0][3] = 1;
        bitMask[2][2] = 1;
        bitMask[2][6] = 1;
        bitMask[3][8] = 1;
        bitMask[5][0] = 1;
        bitMask[6][2] = 1;
        bitMask[6][6] = 1;
        bitMask[8][5] = 1;

        return bitMask;
    }
}
