package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
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
    @FXML
    private AnchorPane rejoinAnchorPane;
    @FXML
    private AnchorPane rejoinedAnchorPane;
    @FXML
    private AnchorPane impossibleEventAnchorPane;

    private GraphicUserInterface gui = null;
    private List<Integer> players = Arrays.asList(2, 3, 4);
    private List<Integer> commons = Arrays.asList(1, 2);
    private static final int MAX_NICKNAME = 20;

    public void setNickname() {
        if(gui != null) {
            String text = nicknameTextField.getText();
            if(text != null && !text.equals("")) {
                if(text.length() > MAX_NICKNAME) {
                    text = text.substring(0, MAX_NICKNAME);
                }
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

    public void rejoinMatch() {
        gui.rejoinMatch();
    }

    public void rejoinScreen() {
        changePane(nicknameAnchorPane, rejoinAnchorPane);
    }

    public void rejoinedMatch() {
        System.out.println("Changing panes...");
        changePane(rejoinAnchorPane, rejoinedAnchorPane);
    }

    public void invalidPlayerNickname() {
        changePane(rejoinAnchorPane, impossibleEventAnchorPane);
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public void changePane(AnchorPane oldAP, AnchorPane newAP) {
        oldAP.setDisable(true);
        System.out.println("a");
        oldAP.setVisible(false);
        System.out.println("b");
        newAP.setDisable(false);
        System.out.println("c");
        newAP.setVisible(true);
        System.out.println("d");
    }

}
