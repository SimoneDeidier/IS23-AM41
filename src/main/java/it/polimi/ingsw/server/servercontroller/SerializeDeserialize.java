package it.polimi.ingsw.server.servercontroller;

import com.google.gson.Gson;

public class SerializeDeserialize {

    private final TCPMessageController tcpMessageController;
    private final Gson gson = new Gson();

    public SerializeDeserialize() {
        tcpMessageController = new TCPMessageController();
    }

    public String deserialize(String input) {

        System.out.println("New message in input.");
        TCPMessage inputMsg = gson.fromJson(input, TCPMessage.class);
        System.out.println("Header: " + inputMsg.getHeader() + ".");
        TCPMessage outMsg = null; // tcpMessageController.check(inputMsg);
        return gson.toJson(outMsg);

    }

}
