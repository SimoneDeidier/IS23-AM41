package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.TCPMessageController;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;

import java.io.Serializable;

public class ClientControllerTCP implements ClientController {

    private final TCPMessageController tcpMessageController;
    private UserInterface userInterface = null;
    private String playerNickname;

    public ClientControllerTCP(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }

    public void startUserInterface(String uiType) {
        switch (uiType) {
            case "gui" -> userInterface = new GraphicUserInterface();
            case "tui" ->  userInterface = new TextUserInterface();
        }
        userInterface.setClientController(this);
        Thread userInterfaceThread = new Thread(() -> userInterface.run());
        userInterfaceThread.start();
        try {
            userInterfaceThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        Body body = new Body();
        body.setPlayerNickname(nickname);
        tcpMessageController.printTCPMessage("Presentation", body);
    }

    @Override
    public void getParameters() {
        userInterface.getGameParameters();
    }

    @Override
    public void invalidNickname() {
        userInterface.invalidNickname();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        Body body = new Body();
        body.setPlayerNickname(this.playerNickname);
        body.setNumberOfPlayers(numPlayers);
        body.setOnlyOneCommon(numCommons == 1);
        tcpMessageController.printTCPMessage("Create Lobby", body);
    }

    @Override
    public void nicknameAccepted() {
        userInterface.nicknameAccepted();
    }

    @Override
    public void lobbyCreated() {
        userInterface.lobbyCreated();
    }

    @Override
    public void waitForLobby() {
        userInterface.waitForLobby();
    }

}
