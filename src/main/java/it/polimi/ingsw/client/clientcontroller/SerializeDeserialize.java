package it.polimi.ingsw.client.clientcontroller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionTCP;
import it.polimi.ingsw.interfaces.SerializeDeserializeInterface;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;

public class SerializeDeserialize implements SerializeDeserializeInterface {

    private final ConnectionTCP connectionTCP;
    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();

    public SerializeDeserialize(ConnectionTCP connectionTCP) {
        this.connectionTCP = connectionTCP;
        this.tcpMessageController = new TCPMessageController(this);
    }

    @Override
    public void deserialize(String input) throws IOException {
        TCPMessage message = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(message);
    }

    @Override
    public void serialize(TCPMessage message) {
        String outMsg = gson.toJson(message);
        connectionTCP.getSocketOut().println(outMsg);
        connectionTCP.getSocketOut().flush();
    }

    @Override
    public void closeConnection() {
        connectionTCP.closeConnection();
    }

    public void startUserInterface(String uiType) {
        tcpMessageController.startUserInterface(uiType);
    }

}
