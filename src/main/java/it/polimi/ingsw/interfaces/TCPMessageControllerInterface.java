package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.servercontroller.SerializeDeserialize;

public interface TCPMessageControllerInterface {

    void readTCPMessage(TCPMessage message);

    void printTCPMessage(String header, Body body);

}
