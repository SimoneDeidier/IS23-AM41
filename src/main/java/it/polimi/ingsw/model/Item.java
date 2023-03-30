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

    //Da cancellare? Mi sembra useless,usiamo l'altro in itemsBag e non mi sembra che creiamo altri item da qualche altra parte
    public Item(Item item) {
        this.type = item.getType();
        this.color = item.getColor();
    }

}
