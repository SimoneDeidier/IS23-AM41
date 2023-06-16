package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;
import it.polimi.ingsw.interfaces.SerializeDeserializeInterface;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.rmi.RemoteException;

public class SerializeDeserialize implements SerializeDeserializeInterface {

    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();
    private final SocketManager socketManager;

    public SerializeDeserialize(SocketManager socketManager) {
        tcpMessageController = new TCPMessageController(socketManager, this);
        this.socketManager = socketManager;
    }

    public void deserialize(String input) throws IOException {
        TCPMessage inputMsg = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(inputMsg);
    }

    public void serialize(TCPMessage message) {
        String outMsg = gson.toJson(message);
        socketManager.getSocketOutput().println(outMsg);
        socketManager.getSocketOutput().flush();
    }

    public void closeConnection() throws IOException {
        System.out.println("CHIAMO LA CLOSE CONNECTION DA SOCKET MANAGER");
        socketManager.closeConnection();
    }

}
