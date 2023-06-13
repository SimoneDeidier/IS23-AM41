package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    public PersonalTargetCard(int personal) throws IOException, URISyntaxException {
        this.personalNumber=personal;

        Gson gson = new Gson();
        File jsonFile = new File("src/main/java/it/polimi/ingsw/server/model/personalTargetCards.json");
        FileUtils.copyInputStreamToFile(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("json/PersonalTargetCards.json")), jsonFile);
        String jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);

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

    public int getPersonalNumber() {
        return personalNumber;
    }
}
