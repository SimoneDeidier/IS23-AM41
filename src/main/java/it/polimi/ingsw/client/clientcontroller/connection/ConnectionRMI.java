package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.PingThreadClientRmiToServer;
import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerRMI;
import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ConnectionRMI extends UnicastRemoteObject implements InterfaceClient, Serializable, Connection {
    private final int PORT;
    private final String IP;
    private InterfaceServer stub;
    private ClientController controller;

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
        try {
            controller.updateView(newView);
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnectUser(int whichMessageToShow) throws RemoteException {
        //todo
        //tell the controller to show an error page, prompting the user to restart the client in order to join a new game

    }

    @Override
    public void confirmConnection(boolean typeOfLobby) throws RemoteException {
        PingThreadClientRmiToServer pingThread = new PingThreadClientRmiToServer(stub,this); //Starting the thread for pinging the server
        pingThread.start();
        if(typeOfLobby) {
            controller.playerRestored();
        }
        else{
            controller.nicknameAccepted();
        }
    }

    @Override
    public void receiveMessage(String sender, String message, String localDateTime) throws RemoteException {
        controller.receiveMessage(message,sender,localDateTime);
    }

    @Override
    public void wrongMessageWarning(String message) throws RemoteException {
        controller.wrongReceiver();
    }

    @Override
    public void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException {
        controller.setPersonalTargetCardNumber(whichPersonal);
        controller.setCommonGoalList(commonTargetCardList);
        try {
            controller.loadGameScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showEndGame(NewView newView) throws RemoteException {
        //tell the controller to make the view show the end game screen
    }

    @Override
    public void lobbyCreated(boolean typeOfGame) throws RemoteException {
        PingThreadClientRmiToServer pingThread = new PingThreadClientRmiToServer(stub,this); //Starting the thread for pinging the server
        pingThread.start();
        if(typeOfGame)
            controller.lobbyCreated();
        else
            controller.lobbyCreated();
    }

    @Override
    public void waitForLobbyCreation() throws RemoteException {
        controller.waitForLobby();
    }

    @Override
    public void askParametersAgain() {
        controller.wrongParameters();
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

    public void sendMoveToServer(Body body) {
        try {
            stub.executeMove(body);
        } catch (RemoteException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void incorrectMove() {
        controller.incorrectMove();
    }
}
