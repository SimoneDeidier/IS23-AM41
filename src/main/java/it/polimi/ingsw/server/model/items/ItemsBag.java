package it.polimi.ingsw.server.model.items;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class Itemsbag is a singleton, since there is only one for game. It needs to be reset at the end of any game, in order to be ready for
 * the next one
 */
public class ItemsBag implements Serializable {
    private static ItemsBag instance = null;
    private List<Item> itemList;
    private final static int ITEMNUMBERFORTYPE = 22;
    private final static int INITIAL_CAPACITY = 132;

    /**
     * The constructor is private due to the design pattern Singleton
     */
    private ItemsBag() {
        itemList = new ArrayList<>(INITIAL_CAPACITY);
    }

    /**
     * getItemsBag is a static method used instead of the Constructor, due to the design pattern Singleton
     * @return the current ItemsBag if it already exists or creates a new ItemsBag if it still wasn't created
     */
    public static ItemsBag getItemsBag() {
        if(instance == null) {
            instance = new ItemsBag();
        }
        return instance;
    }

    /**
     * fills the itemsBag with all the items needed for a game
     */
    public void setupBag() {
        for(ItemColor c : ItemColor.values()) {
            for(int i = 0; i < ITEMNUMBERFORTYPE; i ++) {
                itemList.add(new Item(c));
            }
        }
    }

    /**
     * @return how many items are left in the ItemBag at the moment
     */
    public int getRemainingItemsCount() {
        return itemList.size();
    }

    /**
     * It picks an Item in the bag, removing it from it and returning it to be added to the Board
     * @return a random selected Item in the bag
     */
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

    /**
     * resets the ItemsBag in order to prepare it for a new game, it's due to the design pattern Singleton
     */
    public void resetItemsBag(){
        itemList = new ArrayList<>(INITIAL_CAPACITY);
    }

}
