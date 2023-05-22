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

    public void disconnectUser() {
        gui.disconnect();
        close();
    }

    public void openRulebook() {

    }

    public void exit() {
        gui.close();
        close();
    }

    public void close() {
        menuStage.close();
    }

}
