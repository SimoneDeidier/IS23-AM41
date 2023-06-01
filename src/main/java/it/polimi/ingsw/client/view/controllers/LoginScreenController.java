package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    private AnchorPane rejoinedAnchorPane;
    @FXML
    private AnchorPane impossibleEventAnchorPane;
    @FXML
    private AnchorPane playerRestoredAnchorPane;
    @FXML
    private AnchorPane lobbyRestoredAnchorPane;
    @FXML
    private AnchorPane fullLobbyAnchorPane;

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

    public void rejoinedMatch() {
        changePane(nicknameAnchorPane, rejoinedAnchorPane);

    }

    public void invalidPlayerNickname() {
        changePane(nicknameAnchorPane, impossibleEventAnchorPane);
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

    public void wrongParameters() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/WrongParameters.fxml"));
            Stage wrongParametersStage = new Stage();
            try {
                wrongParametersStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            wrongParametersStage.setTitle("WRONG PARAMETERS!");
            wrongParametersStage.setResizable(false);
            wrongParametersStage.initModality(Modality.APPLICATION_MODAL);
            wrongParametersStage.showAndWait();
        });
    }

    public void playerRestored() {
        changePane(nicknameAnchorPane, playerRestoredAnchorPane);
    }

    public void lobbyRestored() {
        changePane(nicknameAnchorPane, lobbyRestoredAnchorPane);
    }

    public void fullLobby() {
        changePane(nicknameAnchorPane, fullLobbyAnchorPane);
    }

}
