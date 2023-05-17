package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.*;

public class LoginScreenController {

    @FXML
    private TextField nicknameTextField;
    @FXML
    private ChoiceBox<Integer> playersChoiceBox;
    @FXML
    private ChoiceBox<Integer> commonsChoiceBox;
    @FXML
    private AnchorPane nicknameAnchorPane;
    @FXML
    private AnchorPane parametersAnchorPane;
    @FXML
    private AnchorPane waitAnchorPane;
    @FXML
    private Text waitForLobbyText;
    @FXML
    private Text insertNickText;

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
        changePane(nicknameAnchorPane, parametersAnchorPane);
        playersChoiceBox.getItems().addAll(players);
        commonsChoiceBox.getItems().addAll(commons);
    }

    public void sendParameters() {
        Integer numPlayers = playersChoiceBox.getSelectionModel().getSelectedItem();
        Integer numCommons = commonsChoiceBox.getSelectionModel().getSelectedItem();
        if(numPlayers != null && numCommons != null) {
            gui.sendParameters(numPlayers, numCommons);
        }
    }

    public void invalidNickname() {
        for(Node n : nicknameAnchorPane.getChildren()) {
            if(Objects.equals(n.getId(), "insertNickText")) {
                n.setVisible(false);
            }
            else if(Objects.equals(n.getId(), "invalidNickText")) {
                n.setVisible(true);
            }
        }
    }

    public void nicknameAccepted() {
        changePane(nicknameAnchorPane, waitAnchorPane);
    }

    public void lobbyCreated() {
        changePane(parametersAnchorPane, waitAnchorPane);
    }

    public void waitForLobby() {
        insertNickText.setVisible(false);
        waitForLobbyText.setVisible(true);
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public void changePane(AnchorPane oldAP, AnchorPane newAP) {
        oldAP.setDisable(true);
        oldAP.setVisible(false);
        newAP.setDisable(false);
        newAP.setVisible(true);
    }

}
