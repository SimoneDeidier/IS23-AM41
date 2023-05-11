package it.polimi.ingsw.client;

import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.client.clientontroller.ClientController;
import it.polimi.ingsw.server.model.PersonalTargetCard;
import it.polimi.ingsw.server.model.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ConnectionRMI extends Connection implements InterfaceClient, Serializable {
    private int PORT;
    private String IP;
    private InterfaceServer stub;
    private ClientController controller;

    public ConnectionRMI(String ip, int port) {
        this.IP = ip;
        this.PORT = port;
    }

    @Override
    public void startConnection(){
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(IP, PORT);
            // Looking up the registry for the remote object
            stub = (InterfaceServer) registry.lookup("serverInterface");
            /*controller.askNickname(false)*/
            stub.presentation(this, "Samuele");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askForNewNickname() throws RemoteException {
        stub.presentation(this,/*controller.askNickname(true)*/null);
    }

    @Override
    public void updateView(List<Player> playersList) throws RemoteException {

    }

    @Override
    public void askParameters() throws RemoteException {
        //while(true){
        //  int numberOfPlayer= view.askParameter(); MA questo dovrebbe essere fatto dal controller, non server
        //  boolean onlyOneCommon = view.askParameter2();
        //  if(stub.sendParameters(numberOfPlayers,onlyOneCommon)){
        //      controller.confirmCreation();
        //      break;
        //}
        //}
    }

    @Override
    public void disconnectUser(int whichMessageToShow) throws RemoteException {
        //todo
        //tell the controller to show an error page, prompting the user to restart the client in order to join a new game

    }

    @Override
    public void confirmConnection(boolean bool) throws RemoteException {
        //todo
        //tell the controller to show a confirmation the client has had access to the server, boolean false-> a new game, true-> a restored game
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        //tell the controller to show the message in the view
    }

    @Override
    public void wrongMessageWarning(String message) throws RemoteException {
        //tell the controller to show the message in the view explaining the nickname tagged was wrong
    }

    @Override
    public void receivePersonalTargetCard(PersonalTargetCard personalTargetCard) throws RemoteException {
        // todo
    }
}
