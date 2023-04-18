package it.polimi.ingsw.model;

public class TwoPlayersBoard extends BoardFactory {

    private final static int ROWS = 9;
    private final static int COLUMNS = 9;
    private static BoardFactory instance;

    private TwoPlayersBoard() {
        this.itemsBag = ItemsBag.getItemsBag();
        itemsBag.resetItemsBag();
        itemsBag.setupBag();
        bitMask = createBitMask();
        boardMatrix = new Item[9][9];
    }

    public static BoardFactory getTwoPlayersBoard() {
        if(instance == null) {
            instance = new TwoPlayersBoard();
        }
        return instance;
    }

}
