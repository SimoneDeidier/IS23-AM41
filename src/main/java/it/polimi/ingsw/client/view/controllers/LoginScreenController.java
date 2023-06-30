package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.server.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * LoginScreenControlller controls the login screen in the client's GUI.
 */
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
    @FXML
    private Label lobbyLabel;

    private GraphicUserInterface gui = null;
    private List<Integer> players = Arrays.asList(2, 3, 4);
    private List<Integer> commons = Arrays.asList(1, 2);
    private boolean nicknameSetted = false;
    private static final int MAX_NICKNAME = 20;

    /**
     * Shows the page asking for the nickname
     */
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

    /**
     * Shows the page asking for the game parameters
     */
    public void getGameParameters() {
        nicknameSetted = true;
        changePane(nicknameAnchorPane, parametersAnchorPane);
        playersChoiceBox.getItems().addAll(players);
        commonsChoiceBox.getItems().addAll(commons);
    }

    /**
     * Send the game parameters to the GUI
     */
    public void sendParameters() {
        Integer numPlayers = playersChoiceBox.getSelectionModel().getSelectedItem();
        Integer numCommons = commonsChoiceBox.getSelectionModel().getSelectedItem();
        if(numPlayers != null && numCommons != null) {
            gui.sendParameters(numPlayers, numCommons);
        }
    }

    /**
     * Warns the user that the chosen nickname is not valid
     */
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

    /**
     * Tells the user that the nickname was accepted
     * @param nPlayers
     * @param lobby
     */
    public void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby) {
        nicknameSetted = true;
        Platform.runLater(() -> {
            changePane(nicknameAnchorPane, waitAnchorPane);
            String text = lobbyLabel.getText();
            StringBuilder stringBuilder = new StringBuilder(text);
            stringBuilder.append("   ").append(nPlayers).append("\n\n");
            for(String s : lobby.keySet()) {
                stringBuilder.append(" - ").append(s);
                if(!lobby.get(s)) {
                    stringBuilder.append(" - DISCONNECTED!");
                }
                stringBuilder.append("\n");
            }
            lobbyLabel.setText(stringBuilder.toString());
        });
    }

    /**
     * Tells the user that the lobby was created
     *
     * @param nPlayers number of players
     * @param lobby the created lobby
     */
    public void lobbyCreated(int nPlayers, Map<String, Boolean> lobby) {
        Platform.runLater(() -> {
            changePane(parametersAnchorPane, waitAnchorPane);
            String text = lobbyLabel.getText();
            StringBuilder stringBuilder = new StringBuilder(text);
            stringBuilder.append("   ").append(nPlayers).append("\n\n");
            for(String s : lobby.keySet()) {
                stringBuilder.append(" - ").append(s);
                if(!lobby.get(s)) {
                    stringBuilder.append(" - DISCONNECTED!");
                }
                stringBuilder.append("\n");
            }
            lobbyLabel.setText(stringBuilder.toString());
        });
    }

    /**
     * Tells the user that it's waiting for a lobby
     */
    public void waitForLobby() {
        insertNickText.setVisible(false);
        waitForLobbyText.setVisible(true);
    }

    /**
     * Tells the user that he just rejoined a past game
     */
    public void rejoinedMatch() {
        changePane(nicknameAnchorPane, rejoinedAnchorPane);
    }

    /**
     * Tells the user that the chosen nickname is invalid
     */
    public void invalidPlayerNickname() {
        changePane(nicknameAnchorPane, impossibleEventAnchorPane);
    }

    /**
     * Set the gui of the game
     * @param gui the gui of the game
     */
    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    /**
     * Change a GUI pane from the old to the new one
     * @param oldAP old pane to be changed
     * @param newAP new pane to be set
     */
    public void changePane(AnchorPane oldAP, AnchorPane newAP) {
        oldAP.setDisable(true);
        oldAP.setVisible(false);
        newAP.setDisable(false);
        newAP.setVisible(true);
    }

    /**
     * Warns the user that the parameters for the game that he provided are wrong
     */
    public void wrongParameters() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/WrongParameters.fxml"));
            Stage wrongParametersStage = new Stage();
            try {
                wrongParametersStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            wrongParametersStage.setTitle("WRONG PARAMETERS!");
            wrongParametersStage.setResizable(false);
            wrongParametersStage.initModality(Modality.APPLICATION_MODAL);
            wrongParametersStage.showAndWait();
        });
    }

    /**
     * Tells the user that a player has been restored
     */
    public void playerRestored() {
        changePane(nicknameAnchorPane, playerRestoredAnchorPane);
    }

    /**
     * Tells the user that the lobby has been restored
     */
    public void lobbyRestored() {
        changePane(nicknameAnchorPane, lobbyRestoredAnchorPane);
    }

    /**
     * Tells the user that the lobby is full
     */
    public void fullLobby() {
        changePane(nicknameAnchorPane, fullLobbyAnchorPane);
    }

    /**
     * Exit from the GUI of the game
     * @param gui
     */
    public void exit(GraphicUserInterface gui) {
        if(nicknameSetted) {
            gui.exit();
        }
        else {
            gui.exitWithoutWaitingDisconnectFromServer();
        }
    }

    /**
     * Tells the user that a user has just connected
     * @param nickname nickname of the connected user
     */
    public void userConnected(String nickname) {
        Platform.runLater(() -> {
            String text = lobbyLabel.getText();
            StringBuilder stringBuilder = new StringBuilder(text);
            stringBuilder.append(" - ").append(nickname).append("\n");
            lobbyLabel.setText(stringBuilder.toString());
        });
    }

    /**
     * Tells the user that another user has disconnected from the lobby
     * @param nickname nickname of the disconnected user
     */
    public void disconnectedFromLobby(String nickname) {
        Platform.runLater(() ->{
            String text = lobbyLabel.getText();
            String[] lines = text.split("\n");
            StringBuilder stringBuilder = new StringBuilder();
            for(String line : lines) {
                if(!line.contains(nickname)) {
                    stringBuilder.append(line).append("\n");
                }
                else {
                    stringBuilder.append(line).append(" - DISCONNECTED!\n");
                }
            }
            lobbyLabel.setText(stringBuilder.toString());
        });
    }

    /**
     * Tells the user that another player rejoined the game
     *
     * @param nickname nickname of the player that rejoined the game
     */
    public void userRejoined(String nickname) {
        Platform.runLater(() ->{
            String text = lobbyLabel.getText();
            String[] lines = text.split("\n");
            StringBuilder stringBuilder = new StringBuilder();
            for(String line : lines) {
                if(!line.contains(nickname)) {
                    stringBuilder.append(line).append("\n");
                }
                else {
                    stringBuilder.append(line.replace(" - DISCONNECTED!", "")).append("\n");
                }
            }
            lobbyLabel.setText(stringBuilder.toString());
        });
    }

}
