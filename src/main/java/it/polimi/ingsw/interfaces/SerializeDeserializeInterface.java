package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

public interface SerializeDeserializeInterface {

    /**
     * Deserializes the input string.
     *
     * @param input The input string to be deserialized.
     * @throws IOException if an I/O exception occurs.
     * @throws URISyntaxException if the URI syntax is invalid.
     */
    void deserialize(String input) throws IOException, URISyntaxException;

    /**
     * Serializes the TCPMessage object to a JSON string and sends it through the ConnectionTCP instance.
     *
     * @param message The TCPMessage object to be serialized and sent.
     */
    void serialize(TCPMessage message);
    /**
     * Closes the connection.
     */
    void closeConnection() throws IOException;

}
