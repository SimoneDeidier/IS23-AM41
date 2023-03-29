package it.polimi.ingsw.model;

public class TwoPlayersBoard extends Board {

    private static Board instance;

    private TwoPlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
        refillBoard();
    }

    public static Board getTwoPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new TwoPlayersBoard(itemsBag);
        }
        return instance;
    }

}
