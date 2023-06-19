package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

public interface SerializeDeserializeInterface {

    void deserialize(String input) throws IOException, URISyntaxException;

    void serialize(TCPMessage message);

    void closeConnection() throws IOException;

}
