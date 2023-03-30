package it.polimi.ingsw.model;

public class TwoPlayersBoard extends BoardFactory{

    private static BoardFactory instance;

    private TwoPlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
        boardMatrix = new Item[9][9];
    }

    public static BoardFactory getTwoPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new TwoPlayersBoard(itemsBag);
        }
        return instance;
    }

}
