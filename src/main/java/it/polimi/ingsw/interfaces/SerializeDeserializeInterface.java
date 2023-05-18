package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.rmi.RemoteException;

public interface SerializeDeserializeInterface {

    void deserialize(String input) throws IOException;

    void serialize(TCPMessage message);

    void closeConnection();

}
