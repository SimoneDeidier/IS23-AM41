package it.polimi.ingsw.client.clientontroller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ConnectionTCP;
import it.polimi.ingsw.interfaces.SerializeDeserializeInterface;
import it.polimi.ingsw.messages.TCPMessage;

public class SerializeDeserialize implements SerializeDeserializeInterface {

    private final ConnectionTCP connectionTCP;
    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();

    public SerializeDeserialize(ConnectionTCP connectionTCP) {
        this.connectionTCP = connectionTCP;
        this.tcpMessageController = new TCPMessageController(this);
    }

    @Override
    public void deserialize(String input) {
        TCPMessage message = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(message);
    }

    @Override
    public void serialize(TCPMessage message) {

    }

    @Override
    public void closeConnection() {

    }
}
