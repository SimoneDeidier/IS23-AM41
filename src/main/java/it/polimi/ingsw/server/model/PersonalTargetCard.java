package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Class PersonalTargetCard represents a card each player gets, stating where he should put Item of a given color
 * in order to score additional points
 * For each color we have the row and the column a item of the certain color should be inserted to successfully acquire points
 */
public class PersonalTargetCard implements Serializable {
    int personalNumber;
    final int pinkRow;
    final int pinkCol;
    final int light_blueRow;
    final int light_blueCol;
    final int yellowRow;
    final int yellowCol;
    final int blueRow;
    final int blueCol;
    final int whiteRow;
    final int whiteCol;
    final int greenRow;
    final int greenCol;

    /**
     * The constructor for a personalTargetCard looks in the json personalTargetCards.json for setting the corresponding rows and columns
     * to the different colors
     * @param personal represents according to which personal in the game the attributes should be set accordingly
     * @throws IOException if any problem happens during the reading of the file
     * @throws URISyntaxException if any problem happens during the reading of the file
     */
    public PersonalTargetCard(int personal) throws IOException, URISyntaxException {
        this.personalNumber=personal;

        Gson gson = new Gson();
        String jsonString = IOUtils.toString(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("json/PersonalTargetCards.json")), StandardCharsets.UTF_8);

        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        JsonObject TargetCard = jsonArray.get(personal).getAsJsonObject();

        pinkRow = TargetCard.getAsJsonObject("pink").get("row").getAsInt();
        pinkCol = TargetCard.getAsJsonObject("pink").get("column").getAsInt();

        light_blueRow = TargetCard.getAsJsonObject("light_blue").get("row").getAsInt();
        light_blueCol = TargetCard.getAsJsonObject("light_blue").get("column").getAsInt();

        yellowRow = TargetCard.getAsJsonObject("yellow").get("row").getAsInt();
        yellowCol = TargetCard.getAsJsonObject("yellow").get("column").getAsInt();

        blueRow = TargetCard.getAsJsonObject("blue").get("row").getAsInt();
        blueCol = TargetCard.getAsJsonObject("blue").get("column").getAsInt();

        whiteRow = TargetCard.getAsJsonObject("white").get("row").getAsInt();
        whiteCol = TargetCard.getAsJsonObject("white").get("column").getAsInt();

        greenRow = TargetCard.getAsJsonObject("green").get("row").getAsInt();
        greenCol = TargetCard.getAsJsonObject("green").get("column").getAsInt();
    }


    /**
     * Calculate the points made by the user based on the condition of his shelf
     * @param shelf shelf of the user
     * @return the points calculated
     * @throws URISyntaxException
     * @throws IOException
     */
    public int calculatePoints(Shelf shelf) throws URISyntaxException, IOException {
        int correctCards = 0;
        Item item;

        item = shelf.getItemByCoordinates(pinkRow, pinkCol);
        if (item != null && item.getColor() == ItemColor.PINK)
            correctCards++;

        item = shelf.getItemByCoordinates(light_blueRow, light_blueCol);
        if (item != null && item.getColor() == ItemColor.LIGHT_BLUE)
            correctCards++;

        item = shelf.getItemByCoordinates(yellowRow, yellowCol);
        if (item != null && item.getColor() == ItemColor.YELLOW)
            correctCards++;

        item = shelf.getItemByCoordinates(blueRow, blueCol);
        if (item != null && item.getColor() == ItemColor.BLUE)
            correctCards++;

        item = shelf.getItemByCoordinates(whiteRow, whiteCol);
        if (item != null && item.getColor() == ItemColor.WHITE)
            correctCards++;

        item = shelf.getItemByCoordinates(greenRow, greenCol);
        if (item != null && item.getColor() == ItemColor.GREEN)
            correctCards++;

        return switch (correctCards) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 9;
            case 6 -> 12;
            default -> 0;
        };
    }

    /**
     * Return the persona target card identifier number
     * @return the persona target card identifier number
     */
    public int getPersonalNumber() {
        return personalNumber;
    }
}
