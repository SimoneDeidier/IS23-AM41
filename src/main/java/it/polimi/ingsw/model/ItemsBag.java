package it.polimi.ingsw.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsBag {
    private ItemsBag instance = null;
    private List<Item> itemList;

    private ItemsBag() {
        itemList = new ArrayList<Item>(132);
    }

    public ItemsBag getItemsBag() {
        if(instance == null) {
            instance = new ItemsBag();
        }
        return instance;
    }

    public void setupBag() {
        for(ItemColor c : ItemColor.values()) {
            for(int i = 0; i < 22; i ++) {
                itemList.add(new Item(c));
            }
        }
    }

    public int getItemsRemainingCount() {
        return itemList.size();
    }

    public Item pickItem() {
        // random ritorna un item
        return null;
    }

}
