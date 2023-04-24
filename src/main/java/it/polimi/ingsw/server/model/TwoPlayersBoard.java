package it.polimi.ingsw.server.model;

public class TwoPlayersBoard extends BoardFactory {

    private static BoardFactory instance;

    private TwoPlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
        boardMatrix = new Item[ROWS][COLUMNS];
    }

    public static BoardFactory getTwoPlayersBoard() {
        if(instance == null) {
            instance = new TwoPlayersBoard();
        }
        return instance;
    }

}
