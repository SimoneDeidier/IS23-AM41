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

    public void setCommons(boolean isOneCommon, List<Image> images) {
        if(isOneCommon) {
            ImageView imageView = new ImageView(images.get(0));
            imageView.setFitWidth(WIDTH_ONE);
            imageView.setFitHeight(HEIGHT_ONE);
            imageView.setTranslateX(OFFSET_X_ONE);
            imageView.setTranslateX(OFFSET_Y_ONE);
            mainAnchorPane.getChildren().add(imageView);
        }
    }

}
