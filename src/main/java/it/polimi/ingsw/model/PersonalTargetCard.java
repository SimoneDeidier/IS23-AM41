package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PersonalTargetCard {

    public int calculatePoints(Shelf shelf, int personal) {
        int correctCards = 0;
        Gson gson = new Gson();
        File jsonFile = new File("PersonalTargetCards.json");
        String jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);

        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        // select the first object
        JsonObject TargetCard = jsonArray.get(personal).getAsJsonObject();

        int pinkX = TargetCard.getAsJsonObject("pink").get("x").getAsInt();
        int pinkY = TargetCard.getAsJsonObject("pink").get("y").getAsInt();

        if (shelf.getItemByCoordinates(pinkX, pinkY).getColor() == ItemColor.PINK)
            correctCards++;

        int cyanX = TargetCard.getAsJsonObject("cyan").get("x").getAsInt();
        int cyanY = TargetCard.getAsJsonObject("cyan").get("y").getAsInt();

        if (shelf.getItemByCoordinates(cyanX, cyanY).getColor() == ItemColor.CYAN)
            correctCards++;

        int yellowX = TargetCard.getAsJsonObject("yellow").get("x").getAsInt();
        int yellowY = TargetCard.getAsJsonObject("yellow").get("y").getAsInt();

        if (shelf.getItemByCoordinates(yellowX, yellowY).getColor() == ItemColor.YELLOW)
            correctCards++;

        int blueX = TargetCard.getAsJsonObject("blue").get("x").getAsInt();
        int blueY = TargetCard.getAsJsonObject("blue").get("y").getAsInt();

        if (shelf.getItemByCoordinates(blueX, blueY).getColor() == ItemColor.BLUE)
            correctCards++;

        int whiteX = TargetCard.getAsJsonObject("white").get("x").getAsInt();
        int whiteY = TargetCard.getAsJsonObject("white").get("y").getAsInt();

        if (shelf.getItemByCoordinates(whiteX, whiteY).getColor() == ItemColor.WHITE)
            correctCards++;

        int greenX = TargetCard.getAsJsonObject("green").get("x").getAsInt();
        int greenY = TargetCard.getAsJsonObject("green").get("y").getAsInt();

        if (shelf.getItemByCoordinates(greenX, greenY).getColor() == ItemColor.GREEN)
            correctCards++;

        switch (correctCards) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 6;
            case 5:
                return 9;
            case 6:
                return 12;
            default:
                return 0;
        }
    }

}
