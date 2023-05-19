package it.polimi.ingsw.server.model.items;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ItemsBag implements Serializable {
    private static ItemsBag instance = null;
    private List<Item> itemList;
    private final static int ITEMNUMBERFORTYPE = 22;
    private final static int INITIAL_CAPACITY = 132;

    private ItemsBag() {
        itemList = new ArrayList<>(INITIAL_CAPACITY);
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
        if(getRemainingItemsCount()==0){
            return null;
        }
        Random random=new Random();
        Item item=itemList.get(random.nextInt(itemList.size()));
        Item picked = new Item(item.getColor());
        itemList.remove(item);
        return picked;
    }

    public void resetItemsBag(){
        itemList = new ArrayList<>(INITIAL_CAPACITY);
    }

}
