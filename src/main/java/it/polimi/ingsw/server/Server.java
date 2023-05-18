package it.polimi.ingsw.server;

import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.servercontroller.GameController;
import it.polimi.ingsw.server.servercontroller.SocketManager;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements InterfaceServer {

    private static int portRMI;
    private static int portTCP;
    private final GameController controller = GameController.getGameController(this);
    private final Map<String,InterfaceClient> clientMapRMI = new ConcurrentHashMap<>();

    public static boolean parsePortNumber(String[] args) {

        boolean rmiOK = false;
        boolean tcpOK = false;

        for(int i = 0; i < args.length - 1; i++) {
            if(Objects.equals(args[i], "-r") && args[i+1].matches("[0-9]+")) {
                int parsed = Integer.parseInt(args[i+1]);
                if(parsed <= 1024 || parsed > 65535 || (tcpOK && (parsed == portTCP))) {
                    System.out.println("Invalid port number.");
                    return false;
                }
                portRMI = parsed;
                rmiOK = true;
            }
            if(Objects.equals(args[i], "-t") && args[i+1].matches("[0-9]+")) {
                int parsed = Integer.parseInt(args[i+1]);
                if(parsed <= 1024 || parsed > 65535 || (rmiOK && (parsed == portRMI))) {
                    System.out.println("Invalid port number.");
                    return false;
                }
                portTCP = parsed;
                tcpOK = true;
            }
            if(rmiOK && tcpOK) {
                return true;
            }
        }
        return false;

    }

    public static int setPortNumber(String type, int firstPort) {

        int input;
        Scanner in = new Scanner(System.in);

        do {
            System.out.print("Please select a port number for the " + type + " server: ");
            input = in.nextInt();
        }while(input <= 1024 || input > 65535 || input == firstPort);
        return input;

    }

    public static void main(String[] args) {

        // SET THE PORT NUMBER
        if(!parsePortNumber(args)) {
            portRMI = setPortNumber("RMI", 0);
            portTCP = setPortNumber("TCP", portRMI);
        }


        // START RMI SERVER
        InterfaceServer stub =null;
        Server obj =  new Server();
        try {
            stub = (InterfaceServer) UnicastRemoteObject.exportObject(obj, portRMI);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(portRMI);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.bind("serverInterface", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("RMI server ready - listening on port " + portRMI + ".");

        // START TCP SERVER
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portTCP);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.err.println("TCP Server ready - listening on port " + portTCP + ".");
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketManager(socket));
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
        System.out.println("Server socket was closed - server shutting down.");
        executor.shutdown();
        System.out.println("All threads are joined - server shutting down.");

    }

    @Override
    public void presentation(InterfaceClient cl, String nickname) throws RemoteException {
        System.err.println("CHIAMATO PRESENTATION");
        System.err.println(cl);
        try {
            switch(controller.presentation(nickname)) {
                case 1: { //joined a "new" game
                    clientMapRMI.put(nickname,cl);
                    cl.confirmConnection(false);
                } case 2: { //joined a "restored" game
                    clientMapRMI.put(nickname,cl);
                    cl.confirmConnection(true);
                }
                case 0: {  // you're joining but I need another nickname
                    cl.askForNewNickname();
                }
            }
        } catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
            clientMapRMI.put(nickname,cl);
            controller.disconnectAllUsers();
        } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
            clientMapRMI.put(nickname,cl);
            controller.startGame();
            controller.yourTarget();
            controller.updateView();
        } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
            cl.disconnectUser(1);
        } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
            System.err.println("INVOCATA FIRST P EX");
            clientMapRMI.put(nickname,cl);
            System.err.println(cl);
            cl.askParameters();
        } catch (WaitForLobbyParametersException e) {
            cl.waitForLobbyCreation();
        }
    }

    @Override
    public void sendParameters(InterfaceClient cl,int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        if(controller.createLobby(maxPlayerNumber,onlyOneCommonCard))
            cl.lobbyCreated();
        else
            cl.askParameters();
    }

    public void executeMove(Body move) throws RemoteException, InvalidMoveException {
        controller.executeMove(move);
        if(controller.isGameOver()){
            controller.gameOver();
        }
        else{
            controller.updateView();
        }
    }

    public void updateViewRMI(NewView newView) throws RemoteException {
        for(InterfaceClient cl: clientMapRMI.values()){
            cl.updateView(newView);
        }
    }



    public void disconnectAllRMIUsers() throws RemoteException {
        for(Map.Entry<String,InterfaceClient> entry : clientMapRMI.entrySet()){
            entry.getValue().disconnectUser(0);
            clientMapRMI.remove(entry.getKey());
        }
    }

    public void sendPersonalTargetCardsRMI() throws RemoteException {
        for(Player p : controller.getPlayerList()) {
            if(clientMapRMI.containsKey(p.getNickname())) {
                clientMapRMI.get(p.getNickname()).receivePersonalTargetCard(p.getPersonalTargetCard());
            }
        }
    }

    public boolean checkReceiver(String nickname) {
        return clientMapRMI.containsKey(nickname);
    }

    @Override
    public void peerToPeerMsgHandler(String sender, String receiver, String text) throws RemoteException {
        try {
            controller.peerToPeerMsg(sender,receiver,text);
        } catch (InvalidNicknameException e) {
            clientMapRMI.get(sender).wrongMessageWarning(text);
        }
    }

    public void peerToPeerMsg(String sender, String receiver, String text) throws RemoteException {
        clientMapRMI.get(receiver).receiveMessage(sender, text);
    }

    @Override
    public void broadcastMsgHandler(String sender, String text) throws RemoteException {
        controller.broadcastMsg(sender,text);
    }

    public void broadcastMsg(String sender, String text) throws RemoteException {
        for(InterfaceClient interfaceClient: clientMapRMI.values()) {
            interfaceClient.receiveMessage(sender, text);
        }
    }

    @Override
    public void clearRMI() throws RemoteException { //ping from client to server
        //it's empty, we need to check on the other side for RemoteExceptions
    }

    public void gameOverRMI(NewView newView) throws RemoteException{
        for(Map.Entry<String,InterfaceClient> entry : clientMapRMI.entrySet()){
            entry.getValue().showEndGame(newView);
            clientMapRMI.remove(entry.getKey());
        }
    }

}
