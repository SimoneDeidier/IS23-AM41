package it.polimi.ingsw.client.clientcontroller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionTCP;
import it.polimi.ingsw.interfaces.SerializeDeserializeInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.net.URISyntaxException;
/**
 * The SerializeDeserialize class is responsible for serializing and deserializing TCP messages using Gson library.
 */
public class SerializeDeserialize implements SerializeDeserializeInterface {

    private final ConnectionTCP connectionTCP;
    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();
    /**
     * Constructs a new SerializeDeserialize object with the specified ConnectionTCP instance.
     *
     * @param connectionTCP The ConnectionTCP instance.
     */
    public SerializeDeserialize(ConnectionTCP connectionTCP) {
        this.connectionTCP = connectionTCP;
        this.tcpMessageController = new TCPMessageController(this);
    }
    /**
     * Deserializes the input string to a TCPMessage object and passes it to the TCPMessageController for processing.
     *
     * @param input The input string to be deserialized.
     * @throws IOException if an I/O exception occurs.
     * @throws URISyntaxException if the URI syntax is invalid.
     */
    @Override
    public void deserialize(String input) throws IOException, URISyntaxException {
        TCPMessage message = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(message);
    }
    /**
     * Serializes the TCPMessage object to a JSON string and sends it through the ConnectionTCP instance.
     *
     * @param message The TCPMessage object to be serialized and sent.
     */
    @Override
    public void serialize(TCPMessage message) {
        String outMsg = gson.toJson(message);
        connectionTCP.getSocketOut().println(outMsg);
        connectionTCP.getSocketOut().flush();
    }
    /**
     * Closes the connection through the ConnectionTCP instance.
     */
    @Override
    public void closeConnection() {
        connectionTCP.closeConnection();
    }
    /**
     * Starts the user interface with the specified UI type through the TCPMessageController.
     *
     * @param uiType The user interface type (GUI or TCP).
     */
    public void startUserInterface(String uiType) {
        tcpMessageController.startUserInterface(uiType);
    }

    /**
     * Sends a rejoin message to the server through the TCPMessageController.
     */
    public void sendRejoinMsg() {
        Body body = new Body();
        body.setPlayerNickname(tcpMessageController.getPlayerNickname());
        tcpMessageController.printTCPMessage("Re-Join", body);
    }

}
