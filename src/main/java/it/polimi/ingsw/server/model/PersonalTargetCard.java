package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class PersonalTargetCard {
    int whichPersonal;
    final int pinkX;
    final int pinkY;
    final int light_blueX;
    final int light_blueY;
    final int yellowX;
    final int yellowY;
    final int blueX;
    final int blueY;
    final int whiteX;
    final int whiteY;
    final int greenX;
    final int greenY;

    public PersonalTargetCard(int personal) throws IOException, URISyntaxException {
        whichPersonal =personal;
        Gson gson = new Gson();
        File jsonFile = new File(ClassLoader.getSystemResource("jsons/PersonalTargetCards.json").toURI());
        String jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);

        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        JsonObject TargetCard = jsonArray.get(personal).getAsJsonObject();

        pinkX = TargetCard.getAsJsonObject("pink").get("x").getAsInt();
        pinkY = TargetCard.getAsJsonObject("pink").get("y").getAsInt();

        light_blueX = TargetCard.getAsJsonObject("light_blue").get("x").getAsInt();
        light_blueY = TargetCard.getAsJsonObject("light_blue").get("y").getAsInt();

        yellowX = TargetCard.getAsJsonObject("yellow").get("x").getAsInt();
        yellowY = TargetCard.getAsJsonObject("yellow").get("y").getAsInt();

        blueX = TargetCard.getAsJsonObject("blue").get("x").getAsInt();
        blueY = TargetCard.getAsJsonObject("blue").get("y").getAsInt();

        whiteX = TargetCard.getAsJsonObject("white").get("x").getAsInt();
        whiteY = TargetCard.getAsJsonObject("white").get("y").getAsInt();

        greenX = TargetCard.getAsJsonObject("green").get("x").getAsInt();
        greenY = TargetCard.getAsJsonObject("green").get("y").getAsInt();
    }


    public int calculatePoints(Shelf shelf) throws URISyntaxException, IOException {
        int correctCards = 0;
        Item item;

        item = shelf.getItemByCoordinates(pinkX, pinkY);
        if (item != null && item.getColor() == ItemColor.PINK)
            correctCards++;

        item = shelf.getItemByCoordinates(light_blueX, light_blueY);
        if (item != null && item.getColor() == ItemColor.LIGHT_BLUE)
            correctCards++;

        item = shelf.getItemByCoordinates(yellowX, yellowY);
        if (item != null && item.getColor() == ItemColor.YELLOW)
            correctCards++;

        item = shelf.getItemByCoordinates(blueX, blueY);
        if (item != null && item.getColor() == ItemColor.BLUE)
            correctCards++;

        item = shelf.getItemByCoordinates(whiteX, whiteY);
        if (item != null && item.getColor() == ItemColor.WHITE)
            correctCards++;

        item = shelf.getItemByCoordinates(greenX, greenY);
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

    public int getWhichPersonal() {
        return whichPersonal;
    }
}
