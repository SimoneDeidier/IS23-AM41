package it.polimi.ingsw.client.clientcontroller.controller;

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

    void setPersonalTargetCardNumber(int personalTargetCardNumber);

    void loadGameScreen() throws IOException;
}