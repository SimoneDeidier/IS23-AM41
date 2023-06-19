package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MenuController {

    private Stage menuStage = null;

    private GraphicUserInterface gui = null;

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public void setMenuStage(Stage menuStage) {
        this.menuStage = menuStage;
    }


    public void openGit() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/SimoneDeidier/IS23-AM41").toURI());
        }
        catch (IOException | URISyntaxException e) {
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/CouldNotReachSite.fxml"));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(loader.load()));
                } catch (IOException ex) {
                    System.err.println("A crash occurred when loading the scene, please restart the software!");
                }
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("My Shelfie - Could not reach website!");
                stage.show();
            });
        }
    }

    public void openWebsite() {
        try {
            Desktop.getDesktop().browse(new URL("https://www.craniocreations.it/prodotto/my-shelfie").toURI());
        }
        catch (IOException | URISyntaxException e) {
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/CouldNotReachSite.fxml"));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(loader.load()));
                } catch (IOException ex) {
                    System.err.println("A crash occurred when loading the scene, please restart the software!");
                }
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("My Shelfie - Could not reach website!");
                stage.show();
            });
        }
    }

    public void exit() {
        gui.exit();
        close();
    }

    public void close() {
        menuStage.close();
    }

}
