package it.polimi.ingsw.client.clientontroller.controller;

import it.polimi.ingsw.client.clientontroller.connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public interface ClientController {

    void startUserInterface(String uiType);
    void sendNickname(String nickname);
    void getParameters();

}
