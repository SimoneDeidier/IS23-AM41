package it.polimi.ingsw.servercontroller;

public class SocketManager {

    private SocketManager instance = null;
    private SerializeDeserialize serializeDeserialize;

    private SocketManager(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
    }

    public SocketManager getSocketManager(SerializeDeserialize serializeDeserialize) {
        if(instance == null) {
            instance = new SocketManager(serializeDeserialize);
        }
        return instance;
    }

}
