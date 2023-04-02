package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonThreeColumnsTest {


    @Test
    public void check() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException {
        Shelf shelf = new Shelf();

        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.PINK);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.BLUE);
        Item i5 = new Item(ItemColor.WHITE);
        Item i6 = new Item(ItemColor.YELLOW);



        List<Item> itemsAllColor = new ArrayList<Item>();
        Collections.addAll(itemsAllColor, i1, i2, i3, i4, i5, i6);

        List<Item> items1 = new ArrayList<Item>();
        List<Item> items2 = new ArrayList<Item>();
        List<Item> items3 = new ArrayList<Item>();
        List<Item> items4 = new ArrayList<Item>();
        List<Item> items5 = new ArrayList<Item>();


        Collections.addAll(items1, i1, i6, i1);
        Collections.addAll(items2, i3, i5, i1);
        Collections.addAll(items3, i6, i6, i4);


        shelf.insertItems(0, items1);
        shelf.insertItems(0, items3);
        //la colonna 0 soddisfa la condizione

        shelf.insertItems(1, items1);
        shelf.insertItems(1, items2);
        //la colonna 1 non soddisfa la condizione


        shelf.insertItems(2, items1);
        shelf.insertItems(2, items3);
        //la colonna 2 soddisfa la condizione

        shelf.insertItems(3, items1);
        shelf.insertItems(3, items2);
        //la colonna 3 non soddisfa la condizione


        shelf.insertItems(4, items1);
        shelf.insertItems(4, items3);
        //la colonna 4 soddisfa la condizione

        String nickname = "simo";
        Player player = new Player(nickname);

        CommonTargetCard card = new CommonThreeColumns();

        player.addCommonTargetCard(card);

        card.check(shelf);





    }

}