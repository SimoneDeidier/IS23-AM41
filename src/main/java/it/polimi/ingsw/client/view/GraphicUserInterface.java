package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.GameScreenController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GraphicUserInterface extends Application implements UserInterface {

    private static ClientController clientController;
    private static Stage guiStage;
    private static FXMLLoader loader;
    private static LoginScreenController loginScreenController;
    private static GameScreenController gameScreenController;

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        GraphicUserInterface.guiStage = stage;
        loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
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
    public void loadGameScreen(int personalTargetCardNumber, String nickname, int personalNumber) {
        Platform.runLater(() -> {
            guiStage.close();
            loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/GameScreen.fxml"));
            try {
                guiStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameScreenController = loader.getController();
            gameScreenController.setGui(this);
            gameScreenController.setPlayerText(nickname, 0);
            gameScreenController.setPersonalTargetCard(personalNumber);
            guiStage.setResizable(false);
            guiStage.setTitle("My Shelfie - Gaming Phase");
            guiStage.show();
        });
    }

}
