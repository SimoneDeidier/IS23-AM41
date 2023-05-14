package it.polimi.ingsw.client.clientontroller;

import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final SerializeDeserialize serializeDeserialize;
    private final ClientController clientController;

    public TCPMessageController(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
        this.clientController = new ClientController();
    }

    @Override
    public void readTCPMessage(TCPMessage message) {
        String header = message.getHeader();
        switch (header) {
            case "" -> System.out.println();
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

}
