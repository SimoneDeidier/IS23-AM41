package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.server.model.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public class TextUserInterface implements UserInterface{

    private ClientController clientController;

    @Override
    public void run() {
        System.out.println("STARTED CLI");
    }

    @Override
    public void setClientController(ClientController clientController) {

    }

    @Override
    public void getGameParameters() {

    }

    @Override
    public void sendNickname(String nickname) {

    }

    @Override
    public void invalidNickname() {

    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {

    }

    @Override
    public void nicknameAccepted() {

    }

    @Override
    public void lobbyCreated() {

    }

    @Override
    public void waitForLobby() {

    }

    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {

    }

    @Override
    public void updateView(List<Player> playerList) throws FileNotFoundException, URISyntaxException {

    }

}

