package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import javafx.stage.Stage;

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

    }

    public void openWebsite() {

    }

    public void exit() {
        gui.exit();
        close();
    }

    public void close() {
        menuStage.close();
    }

}
