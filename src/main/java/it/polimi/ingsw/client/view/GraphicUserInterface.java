package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serializable;

public class GraphicUserInterface extends Application implements UserInterface, Serializable {

    private static ClientController clientController;
    private static Stage guiStage;
    private static LoginScreenController loginScreenController;

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
        System.out.println(loginScreenController);
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

}
