package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemsBag {
    private static ItemsBag instance = null;
    private List<Item> itemList;
    private final static int ITEMNUMBERFORTYPE = 22;
    Random random = new Random();

    private ItemsBag() {
        itemList = new ArrayList<Item>(132);
    }

    public static ItemsBag getItemsBag() {
        if(instance == null) {
            instance = new ItemsBag();
        }
        return instance;
    }

    public void setupBag() {
        for(ItemColor c : ItemColor.values()) {
            for(int i = 0; i < ITEMNUMBERFORTYPE; i ++) {
                itemList.add(new Item(c));
            }
        }
    }

    public int getRemainingItemsCount() {
        return itemList.size();
    }

    public Item pickItem() {
        Item picked = itemList.get(random.nextInt(itemList.size()));
        itemList.remove(picked);
        return picked;
    }

}
