package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ClientController {

    void startUserInterface(String uiType);
    void sendNickname(String nickname);
    void getParameters();
    void invalidNickname();
    void sendParameters(int numPlayers, int numCommons);
    void nicknameAccepted();
    void lobbyCreated();
    void waitForLobby();

    void setPersonalTargetCardNumber(int personalTargetCardNumber);

    void loadGameScreen() throws IOException;

    void sendMessage(String message);

    void receiveMessage(String message, String sender, String localDateTime);
}
