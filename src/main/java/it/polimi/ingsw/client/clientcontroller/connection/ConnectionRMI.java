package it.polimi.ingsw.client.clientcontroller.connection;

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
import java.rmi.NoSuchObjectException;
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
    private static final int CLEAR_DELAY_MILLISECONDS = 5000;
    private boolean clientConnected;
    private boolean wasIJustReconnected =false;
    private boolean alreadySetMyCards=false;

    public ConnectionRMI(int port, String IP) throws RemoteException {
        super();
        this.PORT = port;
        this.IP = IP;
    }

    @Override
    public void startConnection(String uiType) {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(IP, PORT);
            System.out.println("POST LOCATE REGISTRY: " + registry);
            // Looking up the registry for the remote object
            stub = (InterfaceServer) registry.lookup("InterfaceServer");
            System.out.println("POST STUB: " + stub);
            clientConnected=true;
            controller = new ClientControllerRMI(this);
            System.out.println("POST CONTROLLER: " + clientConnected + " - " + controller);
            controller.startUserInterface(uiType);
            System.out.println("POST START USER INTERFACE");
        } catch (Exception e) {
            System.out.println("Server is offline or unreachable");
        }
    }

    public void presentation(String nickname) {
        try {
            stub.presentation(this, nickname);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    @Override
    public void askParameters() throws RemoteException {
        controller.getParameters();
    }

    public void sendParameters(int maxPlayerNumber, boolean onlyOneCommon) {
        try {
            stub.sendParameters(this, maxPlayerNumber, onlyOneCommon);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    @Override
    public void askForNewNickname() throws RemoteException {
        controller.invalidNickname();
    }

    @Override
    public void updateView(NewView newView) throws RemoteException {
        if(wasIJustReconnected){
            try {
                controller.loadGameScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            wasIJustReconnected=false;
        }
        try {
            controller.updateView(newView);
        } catch (FileNotFoundException | URISyntaxException e) {
            System.out.println("Unknown problem loading the game screen!");
        }
    }

    @Override
    public void disconnectUser(int whichMessageToShow) throws RemoteException {
        clientConnected=false; //stops the ping thread

        switch(whichMessageToShow){
            case 0 -> {
                try {
                    controller.cantRestoreLobby();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 1 -> {
                controller.alonePlayerWins();
            }
            default -> System.err.println("INCORRECT GOODBYE TCP MESSAGE!");
        }

    }

    @Override
    public void confirmConnection(boolean typeOfLobby) throws RemoteException {
        startClearThread();
        if (typeOfLobby) {
            controller.playerRestored();
        } else {
            controller.nicknameAccepted();
        }
    }

    @Override
    public void receiveMessage(String sender, String message, String localDateTime) throws RemoteException {
        controller.receiveMessage(message, sender, localDateTime);
    }

    @Override
    public void wrongMessageWarning(String message) throws RemoteException {
        controller.wrongReceiver();
    }

    @Override
    public void receiveCards(int whichPersonal, List<String> commonTargetCardList) throws RemoteException {
        if(!alreadySetMyCards) {
            controller.setPersonalTargetCardNumber(whichPersonal);
            controller.setCommonGoalList(commonTargetCardList);
            if(!wasIJustReconnected) {
                try {
                    controller.loadGameScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            alreadySetMyCards=true;
        }
    }

    @Override
    public void lobbyCreated(boolean typeOfGame) throws RemoteException {
        startClearThread();
        if (typeOfGame)
            controller.lobbyCreated();
        else
            controller.lobbyRestored();
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
            stub.peerToPeerMsgHandler(body.getSenderNickname(), body.getReceiverNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    public void sendBroadcastMessage(Body body) {
        try {
            stub.broadcastMsgHandler(body.getSenderNickname(), body.getText(), body.getLocalDateTime());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    public void sendMoveToServer(Body body) {
        try {
            stub.executeMove(body);
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    @Override
    public void incorrectMove() {
        controller.incorrectMove();
    }

    public void startClearThread() throws RemoteException {
        new Thread(() -> {
            while (clientConnected) {
                try {
                    System.out.println("Pingo il server");
                    stub.clearRMI();
                    Thread.sleep(CLEAR_DELAY_MILLISECONDS);
                } catch (RemoteException e) {
                    System.out.println("Server crashato!");
                    controller.serverNotResponding();
                    break;
                } catch (InterruptedException e) {
                    //thread interrupted
                }
            }
        }).start();
    }

    @Override
    public void check() throws RemoteException { //ping called from server to client
        //it's empty, we need to check on the other side for RemoteExceptions
    }

    public void voluntaryDisconnection(){
        try {
            stub.voluntaryDisconnection(controller.getPlayerNickname());
        } catch (RemoteException e) {
            controller.serverNotResponding();
        }
    }

    @Override
    public void rejoinedMatch() throws RemoteException {
        wasIJustReconnected=true;
        controller.rejoinedMatch();
    }

    @Override
    public void fullLobby() throws RemoteException {
        controller.fullLobby();
    }

    @Override
    public void notificationForReconnection(String nickname) throws RemoteException {
        controller.playerReconnected(nickname);
    }

    @Override
    public void notificationForDisconnection(String nickname) throws RemoteException {
        controller.playerDisconnected(nickname);
    }

    public void setClientConnected(boolean clientConnected) {
        this.clientConnected = clientConnected;
    }
    @Override
    public void closeRMI() {
        System.exit(0);
    }
}
