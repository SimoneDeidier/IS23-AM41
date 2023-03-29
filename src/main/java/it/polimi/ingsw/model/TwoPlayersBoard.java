package it.polimi.ingsw.model;

public class TwoPlayersBoard extends Board {

    private Board instance;

    private TwoPlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
    }

    public Board getTwoPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new TwoPlayersBoard(itemsBag);
        }
        return instance;
    }

}
