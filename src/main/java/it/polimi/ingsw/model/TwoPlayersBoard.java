package it.polimi.ingsw.model;

public class TwoPlayersBoard extends BoardFactory{

    private BoardFactory instance;

    private TwoPlayersBoard(ItemsBag itemsBag) {
        this.itemsBag = itemsBag;
        bitMask = createBitMask();
    }

    public BoardFactory getTwoPlayersBoard(ItemsBag itemsBag) {
        if(instance == null) {
            instance = new TwoPlayersBoard(itemsBag);
        }
        return instance;
    }

}
