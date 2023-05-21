package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameScreenController {

    private final static double OFFSET = 13.0;
    private final static double MAX_CHAT_MSG = 194.0;
    private final static int BOARD_DIM = 9;

    private GraphicUserInterface gui;
    private Image personalGoalImage = null;
    private List<Image> commonGoalImages = new ArrayList<>(2);
    private boolean onlyOneCommon;

    @FXML
    private Text playerText;
    @FXML
    private TextArea chatMessageTextArea;
    @FXML
    private VBox chatVBox;
    @FXML
    private GridPane boardGridPane;

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

    public void setCommonTargetCard(List<String> commonTargetCardName) throws URISyntaxException, FileNotFoundException {

        for(String name : commonTargetCardName) {
            File file = new File(ClassLoader.getSystemResource("images/commons/" + name + ".jpg").toURI());
            FileInputStream fis = new FileInputStream(file);
            this.commonGoalImages.add(new Image(fis));
        }
        this.onlyOneCommon = this.commonGoalImages.size() == 1;
    }

    public void showCommonTargetCard() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/CommonView.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                CommonGoalController commonGoalController = loader.getController();
                commonGoalController.setCommons(onlyOneCommon, commonGoalImages);
                stage.setResizable(false);
                stage.setTitle("My Shelfie - Common goals!");
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
        newMsg.setMaxWidth(MAX_CHAT_MSG);
        newMsg.setTextFill(Color.WHITE);
        newMsg.setTranslateX(OFFSET);
        newMsg.setTranslateY(OFFSET);
        newMsg.wrapTextProperty().set(true);
        Platform.runLater(() -> {
            chatVBox.getChildren().add(newMsg);
            System.err.println(chatVBox.getHeight());
        });
    }

    public void setBoardItems(Item[][] board) throws FileNotFoundException, URISyntaxException {
        for(int i = 0; i < BOARD_DIM; i++) {
            for(int j = 0; j < BOARD_DIM; j++) {
                if(board[i][j] != null) {
                    ImageView imgv = new ImageView(randomItemImageByColors(board[i][j].getColor()));
                    boardGridPane.add(imgv, i , j);
                }
            }
        }
    }

    public Image randomItemImageByColors(ItemColor color) throws URISyntaxException, FileNotFoundException {
        Random random = new Random();
        int rand = random.nextInt(3);
        File file = null;
        switch (color) {
            case BLUE -> {
                file = new File(ClassLoader.getSystemResource("images/items/b" + rand + ".png").toURI());
            }
            case GREEN -> {
                file = new File(ClassLoader.getSystemResource("images/items/g" + rand + ".png").toURI());
            }
            case YELLOW -> {
                file = new File(ClassLoader.getSystemResource("images/items/y" + rand + ".png").toURI());
            }
            case WHITE -> {
                file = new File(ClassLoader.getSystemResource("images/items/w" + rand + ".png").toURI());
            }
            case PINK -> {
                file = new File(ClassLoader.getSystemResource("images/items/p" + rand + ".png").toURI());
            }
            case LIGHT_BLUE -> {
                file = new File(ClassLoader.getSystemResource("images/items/lb" + rand + ".png").toURI());
            }
        }
        FileInputStream fis = new FileInputStream(file);
        return new Image(fis);
    }

}
