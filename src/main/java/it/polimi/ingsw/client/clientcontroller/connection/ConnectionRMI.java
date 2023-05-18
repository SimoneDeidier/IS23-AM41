package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.GameLock;
import it.polimi.ingsw.client.PingThread;
import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.PersonalTargetCard;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionRMI implements InterfaceClient, Serializable, Connection {
    private final int PORT;
    private final String IP;
    private InterfaceServer stub;
    private ClientController controller;
    boolean gameStarted=false;
    private final Object lock = new GameLock();

    public ConnectionRMI(String ip, int port) {
        this.IP = ip;
        this.PORT = port;
    }

    @Override
    public void startConnection(String uiType){
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(IP, PORT);
            // Looking up the registry for the remote object
            stub = (InterfaceServer) registry.lookup("serverInterface");
            stub.presentation(this,/*controller.askNickname(false)*/"Mario");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askParameters() throws RemoteException {
        //while(true){
        //  if(stub.sendParameters(controller.askParameter(),controller.askParameter2())){
        //      confirmConnection(false);
        //      break;
        //  }
        //}
    }

    @Override
    public void askForNewNickname() throws RemoteException {
        stub.presentation(this,/*controller.askNickname(true)*/null);
    }

    public void waitForGameStart() {
        synchronized (lock) {
            while (!gameStarted) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateView(NewView newView) throws RemoteException {
        if(!gameStarted){ //on the first updateView it exits the waitingRoom
            synchronized (lock) {
                gameStarted = true;
                lock.notifyAll();
            }
            PingThread pingThread = new PingThread(stub,this); //Starting the thread for pinging the server
            pingThread.start();
        }
        //And now we tell the controller to show the GUI/TUI and the client shouldn't need a loop, it's constantly waiting for the player's input
        //The controller will check if the user is the active player, and if he's the active he'll have two options: send message and make move
        //Otherwise he's only able to send a message until a new updateView
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
        waitForGameStart();
    }

    @Override
    public void receiveMessage(String sender, String message) throws RemoteException {
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

    @Override
    public void showEndGame(NewView newView) throws RemoteException {
        //tell the controller to make the view show the end game screen
    }
}
