package it.polimi.ingsw.model;

public class Item {
    private ItemType type;
    private ItemColor color;

    public Item(ItemColor color) {
        this.color = color;
        switch (color) {
            case GREEN: {
                type = ItemType.CAT;
                break;
            }
            case WHITE: {
                type = ItemType.BOOKS;
                break;
            }
            case YELLOW: {
                type = ItemType.GAMES;
                break;
            }
            case BLUE: {
                type = ItemType.FRAMES;
                break;
            }
            case LIGHT_BLUE: {
                type = ItemType.TROPHIES;
                break;
            }
            case PINK: {
                type = ItemType.PLANTS;
                break;
            }
        }
    }

    public Item(ItemType type) {
        this.type = type;
        switch (type) {
            case CAT: {
                color = ItemColor.GREEN;
                break;
            }
            case BOOKS: {
                color = ItemColor.WHITE;
                break;
            }
            case GAMES: {
                color = ItemColor.YELLOW;
                break;
            }
            case FRAMES: {
                color = ItemColor.BLUE;
                break;
            }
            case TROPHIES: {
                color = ItemColor.LIGHT_BLUE;
                break;
            }
            case PLANTS: {
                color = ItemColor.PINK;
                break;
            }
        }
    }

    public ItemType getType() {
        return type;
    }

    public ItemColor getColor() {
        return color;
    }



}
