package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;

public class SerializeDeserialize {

    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();
    private final SocketManager socketManager;

    public SerializeDeserialize(SocketManager socketManager) {
        tcpMessageController = new TCPMessageController(socketManager, this);
        this.socketManager = socketManager;
    }

    public void deserialize(String input) {
        TCPMessage inputMsg = gson.fromJson(input, TCPMessage.class);
        tcpMessageController.readTCPMessage(inputMsg);
    }

    public void serialize(TCPMessage message) {
        String outMsg = gson.toJson(message);
        socketManager.getSocketOutput().println(outMsg);
        socketManager.getSocketOutput().flush();
    }

    public void closeConnection() {
        socketManager.closeConnection();
    }

}
