package it.polimi.ingsw.client;

import it.polimi.ingsw.InterfaceClient;
import it.polimi.ingsw.InterfaceServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements InterfaceClient {
    static int PORT = 1234;
    public static void main(String[] args) {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            // Looking up the registry for the remote object
            InterfaceServer stub = (InterfaceServer) registry.lookup("serverInterface");
            // Calling the remote method using the obtained object
            //if(stub.getAvailableSlot()==){
            //}
            //System.out.println("Remote method invoked " + logged);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    //QUA RIEMPIRE LA INTERFACCIA
}
