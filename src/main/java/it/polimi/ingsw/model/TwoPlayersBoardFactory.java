package it.polimi.ingsw.model;

public class TwoPlayersBoardFactory extends BoardFactory {

    private static BoardFactory instance;

    private TwoPlayersBoardFactory(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
        boardMatrix = new Item[9][9];
    }

    public static BoardFactory getTwoPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new TwoPlayersBoardFactory(itemsBag);
        }
        return instance;
    }

}
