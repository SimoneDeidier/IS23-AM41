package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

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

    void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals);

    void sendMessage(String message);

    void receiveMessage(String message, String sender, String localDateTime);

    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;

    void disconnect();

    void rejoinMatch();

    void close();
}
