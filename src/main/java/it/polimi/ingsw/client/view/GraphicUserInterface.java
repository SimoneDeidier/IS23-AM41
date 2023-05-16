package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application implements UserInterface {

    private ClientController clientController;

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(clientController);
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
        stage.setScene(new Scene(loader.load()));
        LoginScreenController loginScreenController = loader.getController();
        loginScreenController.setGui(this);
        stage.show();
    }

    @Override
    public void setClientController(ClientController clientController) {
        System.out.println("METODO INVOCATO");
        this.clientController = clientController;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
    }

}
