package it.polimi.ingsw.server.servercontroller;

public class TCPMessage {

    private final String header;
    private final Body body;

    public TCPMessage(String header, Body body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

}
