package it.polimi.ingsw.client.clientcontroller;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerTCP;
import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final SerializeDeserialize serializeDeserialize;
    private final ClientController controller;

    public TCPMessageController(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
        controller = new ClientControllerTCP(this);
    }

    @Override
    public void readTCPMessage(TCPMessage message) throws IOException {
        String header = message.getHeader();
        switch (header) {
            case "Nickname Accepted" -> {
                controller.nicknameAccepted();
            }
            case "Wait for Lobby" -> {
                controller.waitForLobby();
            }
            case "Player Restored" -> {

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
                controller.loadGameScreen();
            }
            case "Update View" -> {
                System.err.println("UPDATE VIEW ARRIVATO");
                controller.nicknameAccepted();
            }
            case "Lobby Created" -> {
                controller.lobbyCreated();
            }
            case "Wrong Parameters" -> {

            }
            case "Incorrect Move" -> {

            }
            case "Wrong Receiver" -> {

            }
            case "New Msg" -> {

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

}