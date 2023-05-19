package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;


public class GameScreenController {

    private GraphicUserInterface gui;
    private Image personalGoalImage = null;

    @FXML
    private Text playerText;
    @FXML
    private TextArea chatMessageTextArea;
    @FXML
    private VBox chatVBox;

    public void initialize() {
        chatVBox.setStyle("-fx-background-color: #442211;");
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public void setPlayerText(String nickname, int points) {
        String text = nickname + " - Total points: " + points;
        playerText.setText(text);
    }

    public void setPersonalTargetCard(int personalNumber) throws URISyntaxException, FileNotFoundException {
        File file = new File(ClassLoader.getSystemResource("images/personal/personal" + personalNumber + ".png").toURI());
        FileInputStream fis = new FileInputStream(file);
        this.personalGoalImage = new Image(fis);
    }

    public void showPersonalTargetCard() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/PersonalGoal.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                PersonalGoalController personalGoalController = loader.getController();
                personalGoalController.setPersonalGoal(personalGoalImage);
                stage.setResizable(false);
                stage.setTitle("My Shelfie - Your Personal Goal!");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendInChat() {
        String message = chatMessageTextArea.getText();
        if(message != null && !message.equals("")) {
            gui.sendMessage(message);
            chatMessageTextArea.setText("");
        }
    }

    public void addMessageInChat(String message, String sender, String localDateTime) {
        String messageText = localDateTime + "\n" + sender + ": " + message;
        Label newMsg = new Label(messageText);
        newMsg.setMaxWidth(194.0);
        newMsg.setTextFill(Color.WHITE);
        newMsg.setTranslateX(13.0);
        newMsg.setTranslateY(13.0);
        newMsg.wrapTextProperty().set(true);
        Platform.runLater(() -> {
            chatVBox.getChildren().add(newMsg);
            System.err.println(chatVBox.getHeight());
        });
    }

}
