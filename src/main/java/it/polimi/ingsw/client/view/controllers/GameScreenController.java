package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private double chatMessageOffsetY = 13.0;

    @FXML
    private Text playerText;
    @FXML
    private AnchorPane chatAnchorPane;
    @FXML
    private TextArea chatMessageTextArea;
    @FXML
    private ImageView chatBackgroundImageView;

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
        String messageText = localDateTime + " - " + sender + ":\n" + message;
        Label newMsg = new Label(messageText);
        newMsg.setTextFill(Color.WHITE);
        newMsg.setTranslateX(13);
        newMsg.setTranslateY(chatMessageOffsetY);
        // todo da sistemare il calcolo dell'offset
        double newOffset = newMsg.getHeight() + 13.0;
        chatMessageOffsetY += newOffset;
        if(chatMessageOffsetY > 700.0) {

            chatAnchorPane.setPrefHeight(chatAnchorPane.getHeight() + newOffset + 9.0);
            chatBackgroundImageView.setFitHeight(chatBackgroundImageView.getFitHeight() + newOffset + 9.0);
        }
        Platform.runLater(() -> chatAnchorPane.getChildren().add(newMsg));
    }

}
