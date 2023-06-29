package it.polimi.ingsw.messages;
/**
 * The TCPMessage class represents a message body used in TCP protocol.
 */
public class TCPMessage {

    private final String header;
    private final Body body;

    /**
     * Constructor of TCPMessage given the parameters.
     * @param header The header of the message.
     * @param body The body of the message.
     */
    public TCPMessage(String header, Body body) {
        this.header = header;
        this.body = body;
    }
    /**
     * Gives the header of the TCP message.
     *
     * @return The header.
     */
    public String getHeader() {
        return header;
    }
    /**
     * Gives the body of the TCP message.
     *
     * @return The body.
     */
    public Body getBody() {
        return body;
    }

}
