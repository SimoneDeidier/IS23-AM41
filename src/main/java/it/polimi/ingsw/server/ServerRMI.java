package it.polimi.ingsw.server;

import it.polimi.ingsw.InterfaceClient;
import it.polimi.ingsw.InterfaceServer;
import it.polimi.ingsw.server.servercontroller.CancelGameException;
import it.polimi.ingsw.server.servercontroller.FirstPlayerException;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI implements InterfaceServer {
    static int PORT = 1234;
    private GameController controller;
    private final List<InterfaceClient> clientList;
    public ServerRMI() throws RemoteException {
        controller = GameController.getGameController(null, null, false);
        this.clientList = new ArrayList<>();
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
    public void hello(InterfaceClient cl) throws RemoteException {
        if(controller.clientConnection()){
            clientList.add(cl);
            try{
                while(true){
                    try {
                        if(controller.clientPresentation(cl.askNickname())) {
                            break;
                        }
                    } catch (CancelGameException e) {
                        //todo Chiudi le connessioni ed il server, come si fa?
                    }
                }
            }
            catch(FirstPlayerException e){
                cl.askParameters();
            }
        }

    }

    @Override
    public boolean sendParameters(int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        return controller.checkGameParameters(maxPlayerNumber,onlyOneCommonCard);
    }
}
