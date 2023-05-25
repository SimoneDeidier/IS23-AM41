package it.polimi.ingsw.client.clientcontroller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerTCP;
import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.net.URISyntaxException;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final SerializeDeserialize serializeDeserialize;
    private final ClientController controller;

    public TCPMessageController(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
        controller = new ClientControllerTCP(this);
    }

    @Override
    public void readTCPMessage(TCPMessage message) throws IOException, URISyntaxException {
        String header = message.getHeader();
        switch (header) {
            case "Nickname Accepted" -> {
                controller.nicknameAccepted();
            }
            case "Wait for Lobby" -> {
                controller.waitForLobby();
            }
            case "Player Restored" -> {
                // todo
            }
            case "Invalid Nickname" -> {
                controller.invalidNickname();
            }
            case "Goodbye" -> {
                closeConnection();
            }
            case "Get Parameters" -> {
                controller.getParameters();
            }
            case "Your Target" -> {
                controller.setPersonalTargetCardNumber(message.getBody().getPersonalCardNumber());
                controller.setCommonGoalList(message.getBody().getCommonTargetCardsName());
                controller.loadGameScreen();
            }
            case "Update View" -> {
                System.err.println("UPDATE VIEW ARRIVATO");
                controller.updateView(message.getBody().getNewView());
            }
            case "Lobby Created" -> {
                controller.lobbyCreated();
            }
            case "Wrong Parameters" -> {
                controller.wrongParameters();
            }
            case "Incorrect Move" -> {
                controller.incorrectMove();
            }
            case "Wrong Receiver" -> {
                controller.wrongReceiver();
            }
            case "New Msg" -> {
                controller.receiveMessage(message.getBody().getText(), message.getBody().getSenderNickname(), message.getBody().getLocalDateTime());
            }
            case "Joined" -> {
                controller.rejoinedMatch();
            }
            case "Invalid Player" -> {
                controller.invalidPlayer();
            }

        }
    }

    @Override
    public void printTCPMessage(String header, Body body) {
        TCPMessage tcpMessage = new TCPMessage(header, body);
        serializeDeserialize.serialize(tcpMessage);
    }

    public void closeConnection() {
        serializeDeserialize.closeConnection();
    }

    public void startUserInterface(String uiType) {
        controller.startUserInterface(uiType);
    }

    public void rejoinMatch() {
        serializeDeserialize.rejoinMatch();
    }

    public String getPlayerNickname() {
        return controller.getPlayerNickname();
    }

}
