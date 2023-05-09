package it.polimi.ingsw.client;

import it.polimi.ingsw.InterfaceClient;
import it.polimi.ingsw.InterfaceServer;
import it.polimi.ingsw.server.servercontroller.NewPersonalView;
import it.polimi.ingsw.server.servercontroller.NewView;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements InterfaceClient {
    static int PORT = 1234;
    private InterfaceServer stub;
    private ClientController controller;
    public static void main(String[] args) {
        try {
            new ClientRMI().startClient();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    private void startClient() throws Exception{
        // Getting the registry
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        // Looking up the registry for the remote object
        stub = (InterfaceServer) registry.lookup("serverInterface");
        stub.hello(this,/*controller.askNickname()*/null);
    }

    @Override
    public String askNickname() throws RemoteException {
        //return controller.askNickname();
        return null;
    }

    @Override
    public void askParameters() throws RemoteException {
        //while(true){
        //  int numberOfPlayer= view.askParameter(); MA questo dovrebbe essere fatto dal controller, non server
        //  boolean onlyOneCommon = view.askParameter2();
        //  if(stub.sendParameters(numberOfPlayers,onlyOneCommon))
        //      break;
        //}
    }

    @Override
    public void updateView(NewView newView) throws RemoteException {

    }

    @Override
    public void updatePersonalView(NewPersonalView newPersonalView) throws RemoteException {

    }


    @Override
    public void disconnectUser() throws RemoteException {
        //todo

    }
}
