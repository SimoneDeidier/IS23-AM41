package it.polimi.ingsw.client.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * OtherPlayersController controls the other players screen in the client's GUI.
 */
public class PersonalGoalController {

    @FXML
    private ImageView personalImageView;

    /**
     * Ser the personal goal that the user has to accomplish
     *
     * @param personalGoal image of the personal goal to be accomplished
     */
    public void setPersonalGoal(Image personalGoal) {
        personalImageView.setImage(personalGoal);
    }

}
