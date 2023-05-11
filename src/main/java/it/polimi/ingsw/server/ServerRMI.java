package it.polimi.ingsw.server;

import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.servercontroller.GameController;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRMI implements InterfaceServer {
    static int PORT = 1234;
    private final GameController controller = GameController.getGameController(null,null,false);
    private final Map<String,InterfaceClient> clientMap;
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
    public void presentation(InterfaceClient cl, String nickname) throws RemoteException {
        clientMap.put(nickname,cl);
        try {
            int result=controller.clientPresentation(nickname); //o uno switch
            if (result==1) { //joined a "new" game
                cl.confirmConnection(false);
            }
            else if (result==2) { //joined a "restored" game
                cl.confirmConnection(true);
            }
            else if(result==0){  // you're joining but I need another nickname
                clientMap.remove(nickname);
                cl.askForNewNickname();
            }
        } catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
            for(Map.Entry<String,InterfaceClient> entry : clientMap.entrySet()){
                entry.getValue().disconnectUser(0);
                clientMap.remove(entry.getKey());
            }
        } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
            controller.startGame();
            for (Map.Entry<String,InterfaceClient> entry : clientMap.entrySet()) {
                entry.getValue().updateView(controller.generateUpdatedView());
                entry.getValue().updatePersonalView(controller.generateUpdatedPersonal(entry.getKey()));
            }
        } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
            clientMap.remove(nickname);
            cl.disconnectUser(1);
        } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
            cl.askParameters();
        }
    }

    @Override
    public boolean sendParameters(int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        return controller.checkGameParameters(maxPlayerNumber,onlyOneCommonCard);
    }

    public boolean executeMove(Body move) throws RemoteException {
        if (controller.checkMove(move)) {
            for (Map.Entry<String, InterfaceClient> entry : clientMap.entrySet()) {
                NewView newView= controller.generateUpdatedView();
                //if newView.getActivePlayer==null
                //--> game over, risultati
                //else
                entry.getValue().updateView(newView);
                entry.getValue().updatePersonalView(controller.generateUpdatedPersonal(entry.getKey()));
            }
            return true;
        }
        return false;
    }

    @Override
    public void sendMessage(InterfaceClient cl,String message) throws RemoteException {
        try {
            String receiver=controller.checkMessageType(message);
            if(receiver==null) { //broadcast message
                for(InterfaceClient interfaceClient: clientMap.values()){
                    interfaceClient.receiveMessage(message);
                }
            }
            else{ //direct message
                cl.receiveMessage(message);
                clientMap.get(receiver).receiveMessage(message);
            }
        } catch (IncorrectNicknameException e) {
            cl.wrongMessageWarning(message);
        }
    }
}
