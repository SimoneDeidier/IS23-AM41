package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class ClientControllerRMI implements ClientController, Serializable {

    private final ConnectionRMI connectionRMI;
    private UserInterface userInterface = null;
    private String playerNickname;
    private int personalCardNumber;

    public ClientControllerRMI(ConnectionRMI connectionRMI) {
        this.connectionRMI = connectionRMI;
    }

    @Override
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        try {
            connectionRMI.presentation(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
        try {
            connectionRMI.sendParameters(numPlayers,numCommons==1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void setPersonalTargetCardNumber(int personalTargetCardNumber) {

    }

    @Override
    public void loadGameScreen() throws IOException {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {

    }
}
