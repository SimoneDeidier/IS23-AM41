package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.server.model.items.Item;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

public class OtherPlayersController {

    @FXML
    private AnchorPane otherPlayersAnchorPane;

    private static final double TRANSLATE_X_ONE = 277.0;
    private static final double TRANSLATE_Y_ONE_SHELF = 89.0;
    private static final double TRANSLATE_Y_ONE_TEXT = 43.0;
    private static final double SHELF_WIDTH_ONE = 407.0;
    private static final double SHELF_HEIGHT_ONE = 413.0;
    private static final double FONT_SIZE = 19.0;


    public void setParameters(Map<String, Item[][]> nickToShelf, Map<String, Integer> nickToPoints, String playerNick) throws URISyntaxException, FileNotFoundException {
        File shelfFile = new File(ClassLoader.getSystemResource("images/shelf.png").toURI());
        FileInputStream fis = new FileInputStream(shelfFile);
        Image shelf = new Image(fis);
        switch (nickToShelf.size()) {
            case 2 -> {
                for(String s : nickToShelf.keySet()) {
                    if(!Objects.equals(s, playerNick)) {
                        ImageView imageView = new ImageView(shelf);
                        imageView.setFitWidth(SHELF_WIDTH_ONE);
                        imageView.setFitHeight(SHELF_HEIGHT_ONE);
                        imageView.setTranslateX(TRANSLATE_X_ONE);
                        imageView.setTranslateY(TRANSLATE_Y_ONE_SHELF);
                        Text text = new Text(s + " - points: " + nickToPoints.get(s));
                        text.setTranslateX(TRANSLATE_X_ONE);
                        text.setTranslateY(TRANSLATE_Y_ONE_TEXT);
                        text.setFont(new Font(FONT_SIZE));
                        otherPlayersAnchorPane.getChildren().add(imageView);
                        otherPlayersAnchorPane.getChildren().add(text);
                    }
                }
            }
            case 3 -> {

            }
            case 4 -> {

            }
        }
    }

}
