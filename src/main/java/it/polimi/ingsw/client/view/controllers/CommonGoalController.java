package it.polimi.ingsw.client.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class CommonGoalController {

    @FXML
    private AnchorPane mainAnchorPane;

    private static final double WIDTH_ONE = 333.0;
    private static final double HEIGHT_ONE = 220.0;
    private static final double OFFSET_X_ONE = 153.0;
    private static final double OFFSET_Y_ONE = 69.0;
    private static final double WIDTH_TWO = 274.0;
    private static final double HEIGHT_TWO = 181.0;
    private static final double OFFSET_Y_TWO = 81.0;
    private static final double OFFSET_X_TWO_LEFT = 31.0;
    private static final double OFFSET_X_TWO_RIGHT = 335.0;


    public void setCommons(boolean isOneCommon, List<Image> images) {
        if(isOneCommon) {
            ImageView imageView = new ImageView(images.get(0));
            imageView.setFitWidth(WIDTH_ONE);
            imageView.setFitHeight(HEIGHT_ONE);
            imageView.setTranslateX(OFFSET_X_ONE);
            imageView.setTranslateY(OFFSET_Y_ONE);
            mainAnchorPane.getChildren().add(imageView);
        }
        else {
            ImageView imageView1 = new ImageView(images.get(0));
            imageView1.setFitWidth(WIDTH_TWO);
            imageView1.setFitHeight(HEIGHT_TWO);
            imageView1.setTranslateY(OFFSET_Y_TWO);
            imageView1.setTranslateX(OFFSET_X_TWO_LEFT);
            mainAnchorPane.getChildren().add(imageView1);
            ImageView imageView2 = new ImageView(images.get(1));
            imageView2.setFitWidth(WIDTH_TWO);
            imageView2.setFitHeight(HEIGHT_TWO);
            imageView2.setTranslateY(OFFSET_Y_TWO);
            imageView2.setTranslateX(OFFSET_X_TWO_RIGHT);
            mainAnchorPane.getChildren().add(imageView2);
        }
    }

}
