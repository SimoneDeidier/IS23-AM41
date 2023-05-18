package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.IOException;
import java.time.LocalDateTime;

public interface UserInterface {

    void run();
    void setClientController(ClientController clientController);
    void getGameParameters();

    void sendNickname(String nickname);

    void invalidNickname();

    void sendParameters(int numPlayers, int numCommons);

    void nicknameAccepted();

    void lobbyCreated();

    void waitForLobby();

    void loadGameScreen(int personalTargetCardNumber, String nickname);

    void sendMessage(String message);

    void receiveMessage(String message, String sender, String localDateTime);
}
