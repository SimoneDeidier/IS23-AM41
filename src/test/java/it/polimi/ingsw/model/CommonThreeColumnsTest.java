package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonThreeColumnsTest {


    @Test
    public boolean check(){
        Shelf shelf = new Shelf();

        Item i1 = new Item(ItemColor.LIGHT_BLUE);
        Item i2 = new Item(ItemColor.PINK);
        Item i3 = new Item(ItemColor.GREEN);
        Item i4 = new Item(ItemColor.BLUE);
        Item i5 = new Item(ItemColor.WHITE);
        Item i6 = new Item(ItemColor.YELLOW);



        List<Item> itemsBag = new ArrayList<Item>();
        Collections.addAll(itemsBag, i1, i2, i3, i4, i5, i6);
        List<Item> items1, items2, items3, items4, items5;

        for( int i = 0; i < 6; i++ ){
            itemsBag.add(i1);
        }


        shelf.insertItems(0, );



    }

}