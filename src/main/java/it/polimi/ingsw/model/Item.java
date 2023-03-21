package it.polimi.ingsw.model;

public class Item {
    private ItemType type;
    private ItemColor color;

    public Item(ItemColor color) {
        this.color = color;
        switch (color) {
            case GREEN: {
                type = ItemType.CAT;
            }
            case WHITE: {
                type = ItemType.BOOKS;
            }
            case YELLOW: {
                type = ItemType.GAMES;
            }
            case BLUE: {
                type = ItemType.FRAMES;
            }
            case LIGHT_BLUE: {
                type = ItemType.TROPHIES;
            }
            case PINK: {
                type = ItemType.PLANTS;
            }
        }
    }

}
