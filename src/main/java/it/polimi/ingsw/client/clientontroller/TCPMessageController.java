package it.polimi.ingsw.client.clientontroller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.client.clientontroller.controller.ClientControllerTCP;
import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final SerializeDeserialize serializeDeserialize;
    private final ClientController controller;

    public TCPMessageController(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
        controller = new ClientControllerTCP(this);
    }

    @Override
    public void readTCPMessage(TCPMessage message) {
        String header = message.getHeader();
        switch (header) {
            case "Nickname Accepted" -> {

            }
            case "Player Restored" -> {

            }
            case "Invalid Nickname" -> {

            }
            case "Goodbye" -> {
                closeConnection();
            }
            case "Get Parameters" -> {

            }
            case "Your Target" -> {

            }
            case "Update View" -> {

            }
            case "Lobby Created" -> {

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
