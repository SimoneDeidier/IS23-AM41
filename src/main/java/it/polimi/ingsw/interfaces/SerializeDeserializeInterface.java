package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.TCPMessage;

public interface SerializeDeserializeInterface {

    void deserialize(String input);

    void serialize(TCPMessage message);

    void closeConnection();

}
