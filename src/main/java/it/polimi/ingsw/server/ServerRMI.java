package it.polimi.ingsw.server;

import it.polimi.ingsw.InterfaceClient;
import it.polimi.ingsw.InterfaceServer;
import it.polimi.ingsw.server.servercontroller.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI implements InterfaceServer {
    static int PORT = 1234;
    private final GameController controller = GameController.getGameController(null,null);
    private Map<String,InterfaceClient> clientMap;
    public ServerRMI() throws RemoteException {
        this.clientMap = new ConcurrentHashMap<>() {
        };
    }
    public static void main( String[] args){
        System.out.println( "Hello from Server!" );
        InterfaceServer stub =null;

        ServerRMI obj = null;
        try {
            obj = new ServerRMI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            stub = (InterfaceServer) UnicastRemoteObject.exportObject(obj, PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.bind("serverInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready");
    }

    //QUA RIEMPIRE LA INTERFACCIA
    @Override
    public boolean hello(InterfaceClient cl, String nickname) throws RemoteException {
        clientMap.put(nickname,cl);
        try {
            if (controller.clientPresentation(nickname)) {
                return true;
            }
            else{
                clientMap.remove(nickname);
                return false;
            }
        } catch (CancelGameException e) {
            for(Map.Entry<String,InterfaceClient> entry : clientMap.entrySet()){
                entry.getValue().disconnectUser();
                clientMap.remove(entry.getKey());
            }
        } catch (GameStartException e) {
            controller.startGame();
            for (Map.Entry<String,InterfaceClient> entry : clientMap.entrySet()) {
                entry.getValue().updateView(controller.generateUpdatedView());
                entry.getValue().updatePersonalView(controller.generateUpdatedPersonal(entry.getKey()));
            }
            //se il gioco è pronto per cominciare si arriva qui
        } catch (FullLobbyException e) {
            cl.disconnectUser();
        } catch (FirstPlayerException e) {
            cl.askParameters();
        }
        return false;
    }

    @Override
    public boolean sendParameters(int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        return controller.checkGameParameters(maxPlayerNumber,onlyOneCommonCard);
    }

    public boolean executeMove(Body move) throws RemoteException {
        if (controller.checkMove(move)) {
            for (Map.Entry<String, InterfaceClient> entry : clientMap.entrySet()) {
                NewView newView= controller.generateUpdatedView();
                entry.getValue().updateView(controller.generateUpdatedView());
                entry.getValue().updatePersonalView(controller.generateUpdatedPersonal(entry.getKey()));
            }
            return true;
        }
        return false;

    }
}
