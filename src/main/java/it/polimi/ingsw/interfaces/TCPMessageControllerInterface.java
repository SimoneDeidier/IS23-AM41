package it.polimi.ingsw.interfaces;

import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.servercontroller.SerializeDeserialize;

import java.io.IOException;
import java.rmi.RemoteException;

public interface TCPMessageControllerInterface {

    void readTCPMessage(TCPMessage message) throws IOException;

    void printTCPMessage(String header, Body body);

}
