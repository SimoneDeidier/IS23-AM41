package it.polimi.ingsw.server;

import it.polimi.ingsw.InterfaceClient;
import it.polimi.ingsw.InterfaceServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI implements InterfaceServer {
    static int PORT = 1234;
    private final List<InterfaceClient> clientList;
    public ServerRMI() throws RemoteException {
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
            e.printStackTrace();;
        }
        System.err.println("Server ready");
    }

    //QUA RIEMPIRE LA INTERFACCIA
}
