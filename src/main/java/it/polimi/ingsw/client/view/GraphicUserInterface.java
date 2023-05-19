package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.GameScreenController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.items.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

public class GraphicUserInterface extends Application implements UserInterface, Serializable {

    private static ClientController clientController;
    private static Stage guiStage;
    private static LoginScreenController loginScreenController;
    private static GameScreenController gameScreenController;

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        GraphicUserInterface.guiStage = stage;
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
        guiStage.setScene(new Scene(loader.load()));
        loginScreenController = loader.getController();
        loginScreenController.setGui(this);
        guiStage.setResizable(false);
        guiStage.setTitle("Welcome to My Shelfie!");
        guiStage.show();
    }

    @Override
    public void setClientController(ClientController clientController) {
        GraphicUserInterface.clientController = clientController;
    }

    @Override
    public void getGameParameters() {
        loginScreenController.getGameParameters();
    }

    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
    }

    @Override
    public void invalidNickname() {
        loginScreenController.invalidNickname();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
         clientController.sendParameters(numPlayers, numCommons);
    }

    @Override
    public void nicknameAccepted() {
        loginScreenController.nicknameAccepted();
    }

    @Override
    public void lobbyCreated() {
        loginScreenController.lobbyCreated();
    }

    @Override
    public void waitForLobby() {
        loginScreenController.waitForLobby();
    }

    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {
        Platform.runLater(() -> {
            guiStage.close();
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/GameScreen.fxml"));
            try {
                guiStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameScreenController = loader.getController();
            gameScreenController.setGui(this);
            gameScreenController.setPlayerText(nickname, 0);
            try {
                gameScreenController.setPersonalTargetCard(personalTargetCardNumber);
            } catch (URISyntaxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                gameScreenController.setCommonTargetCard(commonTargetGoals);
            } catch (URISyntaxException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            guiStage.setResizable(false);;
            guiStage.setTitle("My Shelfie - Gaming Phase");
            guiStage.show();
        });
    }

    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        gameScreenController.addMessageInChat(message, sender, localDateTime);
    }

    @Override
    public void updateView(List<Player> playerList) throws FileNotFoundException, URISyntaxException {
        Item[][] boardMatrix = playerList.get(0).getBoard().getBoardMatrix();
        gameScreenController.setBoardItems(boardMatrix);
    }

}
