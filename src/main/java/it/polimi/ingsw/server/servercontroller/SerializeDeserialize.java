package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SerializeDeserialize {

    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();

    public SerializeDeserialize(SocketManager socketManager) {
        tcpMessageController = new TCPMessageController(socketManager);
    }

    public List<String> deserialize(String input) {

        System.out.println("New message in input.");
        TCPMessage inputMsg = gson.fromJson(input, TCPMessage.class);
        System.out.println("Header: " + inputMsg.getHeader() + ".");
        List<TCPMessage> tcpMessages = tcpMessageController.executeTCPMessage(inputMsg);
        List<String> outMessages = new ArrayList<>();
        for(TCPMessage msg : tcpMessages) {
            outMessages.add(gson.toJson(msg));
        }
        return outMessages;
    }

}
