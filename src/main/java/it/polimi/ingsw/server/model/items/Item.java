package it.polimi.ingsw.server.model.items;

import java.io.Serializable;

public class Item implements Serializable {
    private final ItemColor color;

    public Item(ItemColor color) {
        this.color = color;
    }

    public ItemColor getColor() {
        return color;
    }



}
