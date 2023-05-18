package it.polimi.ingsw.client.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PersonalGoalController {

    @FXML
    private ImageView personalImageView;

    public void setPersonalGoal(Image personalGoal) {
        personalImageView.setImage(personalGoal);
    }

}
