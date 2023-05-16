package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import it.polimi.ingsw.server.servercontroller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application implements Runnable, UserInterface {

    private final ClientController controller;

    public GraphicUserInterface(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
        stage.setScene(new Scene(loader.load()));
        LoginScreenController controller = loader.getController();
        stage.show();
    }
}
