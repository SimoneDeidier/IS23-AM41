package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
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
            // todo mettere alert magari
            e.printStackTrace();
        }
    }

    public void openWebsite() {
        try {
            Desktop.getDesktop().browse(new URL("https://www.craniocreations.it/prodotto/my-shelfie").toURI());
        }
        catch (IOException | URISyntaxException e) {
            // todo mettere alert magari
            e.printStackTrace();
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
