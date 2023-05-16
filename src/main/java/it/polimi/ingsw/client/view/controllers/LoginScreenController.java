package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginScreenController {

    @FXML
    private TextField nicknameTextField;

    private GraphicUserInterface gui = null;

    public void setNickname() {
        if(gui != null) {
            String text = nicknameTextField.getText();
            if(text != null && text != "") {
                gui.sendNickname(text);
            }
        }
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

}
