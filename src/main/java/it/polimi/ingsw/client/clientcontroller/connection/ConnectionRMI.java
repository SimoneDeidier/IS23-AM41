package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.GameLock;
import it.polimi.ingsw.client.PingThread;
import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerRMI;
import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ConnectionRMI extends UnicastRemoteObject implements InterfaceClient, Serializable, Connection {
    private final int PORT;
    private final String IP;
    private InterfaceServer stub;
    private ClientController controller;
    boolean gameStarted=false;
    private final Object lock = new GameLock();

    public ConnectionRMI(int port,String IP) throws RemoteException {
        super();
        this.PORT=port;
        this.IP=IP;
    }

    @Override
    public void startConnection(String uiType){
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(IP, PORT);
            // Looking up the registry for the remote object
            stub = (InterfaceServer) registry.lookup("serverInterface");
            controller=new ClientControllerRMI(this);
            controller.startUserInterface(uiType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void presentation(String nickname) throws RemoteException {
        stub.presentation(this, nickname);
    }

    @Override
    public void askParameters() throws RemoteException {
        controller.getParameters();
    }

    public void sendParameters(int maxPlayerNumber,boolean onlyOneCommon) throws RemoteException{
        stub.sendParameters(this,maxPlayerNumber,onlyOneCommon);
    }

    @Override
    public void askForNewNickname() throws RemoteException {
        controller.invalidNickname();
    }

    @Override
    public void updateView(NewView newView) throws RemoteException {
        if(!gameStarted){ //on the first updateView it starts the ping to the server
            PingThread pingThread = new PingThread(stub,this); //Starting the thread for pinging the server
            pingThread.start();
            gameStarted=true;
        }
        controller.setCommonGoalList(newView.getPlayerList());
        try {
            controller.loadGameScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnectUser(int whichMessageToShow) throws RemoteException {
        //todo
        //tell the controller to show an error page, prompting the user to restart the client in order to join a new game

    }

    @Override
    public void confirmConnection(boolean bool) throws RemoteException {
        //todo
        if(!bool)
            controller.nicknameAccepted();
        //manca caso in cui è player restored
    }

    @Override
    public void receiveMessage(String sender, String message, String localDateTime) throws RemoteException {
        controller.receiveMessage(message,sender,localDateTime);
    }

    @Override
    public void wrongMessageWarning(String message) throws RemoteException {
        //tell the controller to show the message in the view explaining the nickname tagged was wrong
    }

    @Override
    public void receivePersonalTargetCard(int whichPersonal) throws RemoteException {
        controller.setPersonalTargetCardNumber(whichPersonal);
    }

    @Override
    public void showEndGame(NewView newView) throws RemoteException {
        //tell the controller to make the view show the end game screen
    }

    @Override
    public void lobbyCreated() throws RemoteException {
        controller.lobbyCreated();
    }

    @Override
    public void waitForLobbyCreation() throws RemoteException {
        controller.waitForLobby();
    }

    public void sendPrivateMessage(Body body) {
        try {
            stub.peerToPeerMsgHandler(body.getSenderNickname(),body.getReceiverNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendBroadcastMessage(Body body) {
        try {
            stub.broadcastMsgHandler(body.getSenderNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
