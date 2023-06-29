package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;
import it.polimi.ingsw.interfaces.SerializeDeserializeInterface;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.rmi.RemoteException;
/**
 * The SerializeDeserialize class is responsible for serializing and deserializing TCP messages using the Gson library.
 */
public class SerializeDeserialize implements SerializeDeserializeInterface {

    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();
    private final SocketManager socketManager;

    /**
     * Constructs a new SerializeDeserialize instance with the specified SocketManager.
     *
     * @param socketManager The SocketManager instance.
     */
    public SerializeDeserialize(SocketManager socketManager) {
        tcpMessageController = new TCPMessageController(socketManager, this);
        this.socketManager = socketManager;
    }
    /**
     * Deserializes the input string into a TCPMessage object and passes it to the TCP message controller for processing.
     *
     * @param input The input string to deserialize.
     * @throws IOException if an I/O error occurs.
     */
    public void deserialize(String input) throws IOException {
        TCPMessage inputMsg = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(inputMsg);
    }
    /**
     * Serializes the TCPMessage object into a string and sends it through the socket connection.
     *
     * @param message The TCPMessage object to serialize and send.
     */
    public void serialize(TCPMessage message) {
        String outMsg = gson.toJson(message);
        socketManager.getSocketOutput().println(outMsg);
        socketManager.getSocketOutput().flush();
    }
    /**
     * Closes the socket connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void closeConnection() throws IOException {
        socketManager.closeConnection();
    }

}
