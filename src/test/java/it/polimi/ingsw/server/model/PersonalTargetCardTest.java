package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.NotEnoughSpaceInColumnException;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
* Class to test the Personal Target Card class
*/
class PersonalTargetCardTest {

/**
* Test if the points get calculated correctly
*/
    @Test
    void testCalculatePoints() throws NotEnoughSpaceInColumnException, IOException, URISyntaxException {
        PersonalTargetCard personal = new PersonalTargetCard(0);
        Shelf shelf = new Shelf();

        shelf.insertItems(0, Arrays.asList(new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.GREEN), new Item(ItemColor.PINK), new Item(ItemColor.PINK)));
        shelf.insertItems(1, Arrays.asList(new Item(ItemColor.BLUE), new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.YELLOW), new Item(ItemColor.WHITE), new Item(ItemColor.WHITE)));
        shelf.insertItems(2, Arrays.asList(new Item(ItemColor.LIGHT_BLUE), new Item(ItemColor.PINK), new Item(ItemColor.BLUE), new Item(ItemColor.GREEN), new Item(ItemColor.BLUE), new Item(ItemColor.BLUE)));
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

/**
* Tests if number of the personal target card gets returned correctly
*/
    @Test
    void testGetPersonalNumber(){
        try {
            PersonalTargetCard personalTargetCard=new PersonalTargetCard(0);
            assertEquals(0,personalTargetCard.getPersonalNumber());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}