package it.polimi.ingsw.server.model.items;

import java.io.Serializable;

/**
 * The class Item represents the tile placed on the Board. Its most important feature is the color
 */
public class Item implements Serializable {
    private final ItemColor color;

    public Item(ItemColor color) {
        this.color = color;
    }

    public ItemColor getColor() {
        return color;
    }



}
