package it.polimi.ingsw.servercontroller;

public class SerializeDeserialize {

    private SerializeDeserialize instance = null;
    private TCPMessageController tcpMessageController;

    private SerializeDeserialize(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }

    public SerializeDeserialize getSerializeDeserialize(TCPMessageController tcpMessageController) {
        if(instance == null) {
            instance = new SerializeDeserialize(tcpMessageController);
        }
        return instance;
    }

}
