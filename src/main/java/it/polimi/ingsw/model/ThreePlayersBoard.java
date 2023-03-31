package it.polimi.ingsw.model;

public class ThreePlayersBoard extends BoardFactory {

    private static BoardFactory instance;

    private ThreePlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
    }

    public static BoardFactory getThreePlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new ThreePlayersBoard(itemsBag);
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
