package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.TCPMessage;

import java.rmi.RemoteException;

public interface SerializeDeserializeInterface {

    void deserialize(String input) throws RemoteException;

    void serialize(TCPMessage message);

    void closeConnection();

}
