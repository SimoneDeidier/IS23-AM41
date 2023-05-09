package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalTargetCardTest {

    @Test
    void TestCalculatePoints() throws EmptyItemListToInsert, NotEnoughSpaceInColumnException, IOException, URISyntaxException {
        PersonalTargetCard personal = new PersonalTargetCard(0);
        Shelf shelf = new Shelf();

        shelf.insertItems(0, Arrays.asList(new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.PINK), new Item(ItemColor.BLUE)));
        shelf.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelf.insertItems(2, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.LIGHT_BLUE)));
        shelf.insertItems(3, Arrays.asList(new Item(ItemColor.YELLOW), new Item(ItemColor.BLUE), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.YELLOW)));
        shelf.insertItems(4, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN)));

        for(int i = 0; i < 6; i++) {
            System.out.print("| ");
            for(int j = 0; j < 5; j++) {
                System.out.print(shelf.getItemByCoordinates(i, j).getColor() + " ");
            }
            System.out.print("|\n");
        }

        assertEquals(12, personal.calculatePoints(shelf));
    }
}