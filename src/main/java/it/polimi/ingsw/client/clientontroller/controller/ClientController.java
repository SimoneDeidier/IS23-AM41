package it.polimi.ingsw.client.clientontroller.controller;

import it.polimi.ingsw.client.clientontroller.connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public interface ClientController {

    void startUserInterface(String uiType);
    void sendNickname(String nickname);
    void getParameters();

    void invalidNickname();

    void sendParameters(int numPlayers, int numCommons);

    void nicknameAccepted();

    void lobbyCreated();

    void waitForLobby();
}
