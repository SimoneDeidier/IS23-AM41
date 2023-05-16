package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.*;

public class LoginScreenController {

    @FXML
    private TextField nicknameTextField;
    @FXML
    private Text insertNickText;
    @FXML
    private Text commonTargText;
    @FXML
    private Button connectBtn;
    @FXML
    private ChoiceBox<Integer> playersChoiceBox;
    @FXML
    private ChoiceBox<Integer> commonsChoiceBox;
    @FXML
    private Button createLobbyBtn;

    private GraphicUserInterface gui = null;
    private List<Integer> players = Arrays.asList(2, 3, 4);
    private List<Integer> commons = Arrays.asList(1, 2);

    public void setNickname() {
        if(gui != null) {
            String text = nicknameTextField.getText();
            if(text != null && !text.equals("")) {
                gui.sendNickname(text);
            }
        }
    }

    public void getGameParameters() {
        insertNickText.setVisible(false);
        nicknameTextField.setDisable(true);
        nicknameTextField.setVisible(false);
        connectBtn.setDisable(true);
        connectBtn.setVisible(false);
        commonTargText.setVisible(true);
        playersChoiceBox.setDisable(false);
        playersChoiceBox.setVisible(true);
        playersChoiceBox.getItems().addAll(players);
        commonsChoiceBox.setDisable(false);
        commonsChoiceBox.setVisible(true);
        commonsChoiceBox.getItems().addAll(commons);
        createLobbyBtn.setDisable(false);
        createLobbyBtn.setVisible(true);
    }

    public void sendParameters() {
        System.out.println("SEND PARAMETERS");
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

}
