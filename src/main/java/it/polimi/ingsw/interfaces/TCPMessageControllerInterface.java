package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.servercontroller.SerializeDeserialize;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

public interface TCPMessageControllerInterface {
    /**
     * Reads and processes the TCP message received from the server.
     *
     * @param message The TCP message received.
     * @throws IOException if an I/O exception occurs.
     * @throws URISyntaxException if the URI syntax is invalid.
     */
    void readTCPMessage(TCPMessage message) throws IOException, URISyntaxException;
    /**
     * Prints the TCP message with the specified header and body.
     *
     * @param header The header of the TCP message.
     * @param body The body of the TCP message.
     */
    void printTCPMessage(String header, Body body);

}
