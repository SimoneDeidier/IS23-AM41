package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application implements UserInterface {

    private static ClientController clientController;
    private static Stage guiStage;
    private static FXMLLoader loader;
    private static LoginScreenController loginScreenController;

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

}
