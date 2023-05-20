package it.polimi.ingsw.client.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CommonGoalController {
    @FXML
    private ImageView onlyOneCommonView;
    @FXML
    private ImageView firstCommonView;
    @FXML
    private ImageView secondCommonView;

    public void setOnlyOneCommonView(Image commonGoal) {
        onlyOneCommonView.setImage(commonGoal);
        onlyOneCommonView.setVisible(true);
    }
    public void setTwoCommonsView(Image commonGoal,Image commonGoalTwo) {
        firstCommonView.setImage(commonGoal);
        firstCommonView.setVisible(true);
        secondCommonView.setImage(commonGoalTwo);
        secondCommonView.setVisible(true);
    }
}
