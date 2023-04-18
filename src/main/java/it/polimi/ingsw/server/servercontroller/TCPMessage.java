package it.polimi.ingsw.server.servercontroller;

public class TCPMessage {

    private final String header;
    private final Move body;

    public TCPMessage(String header, Move body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public Move getBody() {
        return body;
    }

    public void printTCPMessage() {
        System.out.println("Header: " + header);
        System.out.println("Nickname: " + body.getPlayerNickname());
        System.out.println("Items: " + body.getPositionsPicked());
        System.out.println("Col: " + body.getColumn());
    }
}
