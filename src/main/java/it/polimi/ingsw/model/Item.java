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

    public Item(ItemType type) {
        this.type = type;
        switch (type) {
            case CAT: {
                color = ItemColor.GREEN;
            }
            case BOOKS: {
                color = ItemColor.WHITE;
            }
            case GAMES: {
                color = ItemColor.YELLOW;
            }
            case FRAMES: {
                color = ItemColor.BLUE;
            }
            case TROPHIES: {
                color = ItemColor.LIGHT_BLUE;
            }
            case PLANTS: {
                color = ItemColor.PINK;
            }
        }
    }

    public ItemType getType() {
        return type;
    }

    public ItemColor getColor() {
        return color;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setColor(ItemColor color) {
        this.color = color;
    }


}
