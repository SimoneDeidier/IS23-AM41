package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.server.model.items.Item;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OtherPlayersController {

    @FXML
    private AnchorPane otherPlayersAnchorPane;
    @FXML
    private GridPane oneShelfGridPane;
    @FXML
    private GridPane twoShelfGridPane_L;
    @FXML
    private GridPane twoShelfGridPane_R;
    @FXML
    private GridPane threeShelfGridPane_L;
    @FXML
    private GridPane threeShelfGridPane_C;
    @FXML
    private GridPane threeShelfGridPane_R;

    // two players
    private static final double TRANSLATE_X_ONE = 273.0;
    private static final double TRANSLATE_Y_ONE_SHELF = 89.0;
    private static final double TRANSLATE_Y_ONE_TEXT = 43.0;
    private static final double SHELF_WIDTH_ONE = 414.0;
    private static final double SHELF_HEIGHT_ONE = 413.0;
    private static final double ITEM_DIM_ONE = 43.0;
    private static final double ITEM_TRANSLATE_ONE = 9.0;

    // three players

    private static final double TRANSLATE_X_TWO = 89.0;
    private static final double TRANSLATE_X_OFFSET_TWO = 420.0;
    private static final double TRANSLATE_Y_TWO_SHELF = 143.0;
    private static final double TRANSLATE_Y_TWO_TEXT = 109.0;
    private static final double SHELF_WIDTH_TWO = 362.0;
    private static final double SHELF_HEIGHT_TWO = 361.0;
    private static final double ITEM_DIM_TWO = 38.0;
    private static final double ITEM_TRANSLATE_TWO = 9.0;

    // four players

    private static final double TRANSLATE_X_THREE = 43.0;
    private static final double TRANSLATE_X_OFFSET_THREE = 306.0;
    private static final double TRANSLATE_Y_THREE_SHELF = 243.0;
    private static final double TRANSLATE_Y_THREE_TEXT = 189.0;
    private static final double SHELF_WIDTH_THREE = 262.0;
    private static final double SHELF_HEIGHT_THREE = 261.0;
    private static final double ITEM_DIM_THREE = 28.0;
    private static final double ITEM_TRANSLATE_THREE = 7.0;
    private static final double FONT_SIZE_THREE = 17.0;

    // others

    private static final double FONT_SIZE = 19.0;
    private static final int ROWS = 6;
    private static final int COLS = 5;

    private GameScreenController gsc = null;
    private final List<GridPane> twoGridPanes = new ArrayList<>(2);
    private final List<GridPane> threeGridPanes = new ArrayList<>(3);

    public void initialize() {
        twoGridPanes.add(twoShelfGridPane_L);
        twoGridPanes.add(twoShelfGridPane_R);
        threeGridPanes.add(threeShelfGridPane_L);
        threeGridPanes.add(threeShelfGridPane_C);
        threeGridPanes.add(threeShelfGridPane_R);
    }


    public void setParameters(Map<String, Item[][]> nickToShelf, Map<String, Integer> nickToPoints, String playerNick) throws URISyntaxException, IOException {
        File shelfFile = new File(ClassLoader.getSystemResource("images/shelf1.png").toURI());
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
                        for(int i = 0; i < ROWS; i++) {
                            for(int j = 0; j < COLS; j++) {
                                if(nickToShelf.get(s)[i][j] != null) {
                                    ImageView imgv = new ImageView(gsc.randomItemImageByColors(nickToShelf.get(s)[i][j].getColor()));
                                    imgv.setFitWidth(ITEM_DIM_ONE);
                                    imgv.setFitHeight(ITEM_DIM_ONE);
                                    imgv.setTranslateX(ITEM_TRANSLATE_ONE);
                                    oneShelfGridPane.add(imgv, j, i);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            case 3 -> {
                int count = 0;
                for(String s : nickToShelf.keySet()) {
                    if(!Objects.equals(s, playerNick)) {
                        ImageView imageView = new ImageView(shelf);
                        imageView.setFitWidth(SHELF_WIDTH_TWO);
                        imageView.setFitHeight(SHELF_HEIGHT_TWO);
                        imageView.setTranslateX(TRANSLATE_X_TWO + count * TRANSLATE_X_OFFSET_TWO);
                        imageView.setTranslateY(TRANSLATE_Y_TWO_SHELF);
                        Text text = new Text(s + " - points: " + nickToPoints.get(s));
                        text.setTranslateX(TRANSLATE_X_TWO + count * TRANSLATE_X_OFFSET_TWO);
                        text.setTranslateY(TRANSLATE_Y_TWO_TEXT);
                        text.setFont(new Font(FONT_SIZE));
                        otherPlayersAnchorPane.getChildren().add(imageView);
                        otherPlayersAnchorPane.getChildren().add(text);
                        for(int i = 0; i < ROWS; i++) {
                            for(int j = 0; j < COLS; j++) {
                                if(nickToShelf.get(s)[i][j] != null) {
                                    ImageView imgv = new ImageView(gsc.randomItemImageByColors(nickToShelf.get(s)[i][j].getColor()));
                                    imgv.setFitWidth(ITEM_DIM_TWO);
                                    imgv.setFitHeight(ITEM_DIM_TWO);
                                    imgv.setTranslateX(ITEM_TRANSLATE_TWO);
                                    twoGridPanes.get(count).add(imgv, j, i);
                                }
                            }
                        }
                        count++;
                    }
                    if(count == 2) break;
                }
            }
            case 4 -> {
                int count = 0;
                for(String s : nickToShelf.keySet()) {
                    if(!Objects.equals(s, playerNick)) {
                        ImageView imageView = new ImageView(shelf);
                        imageView.setFitWidth(SHELF_WIDTH_THREE);
                        imageView.setFitHeight(SHELF_HEIGHT_THREE);
                        imageView.setTranslateX(TRANSLATE_X_THREE + count * TRANSLATE_X_OFFSET_THREE);
                        imageView.setTranslateY(TRANSLATE_Y_THREE_SHELF);
                        Text text = new Text(s + " - points: " + nickToPoints.get(s));
                        text.setTranslateX(TRANSLATE_X_THREE + count * TRANSLATE_X_OFFSET_THREE);
                        text.setTranslateY(TRANSLATE_Y_THREE_TEXT);
                        text.setFont(new Font(FONT_SIZE_THREE));
                        otherPlayersAnchorPane.getChildren().add(imageView);
                        otherPlayersAnchorPane.getChildren().add(text);
                        for(int i = 0; i < ROWS; i++) {
                            for(int j = 0; j < COLS; j++) {
                                if(nickToShelf.get(s)[i][j] != null) {
                                    ImageView imgv = new ImageView(gsc.randomItemImageByColors(nickToShelf.get(s)[i][j].getColor()));
                                    imgv.setFitWidth(ITEM_DIM_THREE);
                                    imgv.setFitHeight(ITEM_DIM_THREE);
                                    imgv.setTranslateX(ITEM_TRANSLATE_THREE);
                                    threeGridPanes.get(count).add(imgv, j, i);
                                }
                            }
                        }
                        count++;
                    }
                    if(count == 3) break;
                }
            }
        }
    }

    public void setGsc(GameScreenController gsc) {
        this.gsc = gsc;
    }

}
