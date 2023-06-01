package it.polimi.ingsw.server;

import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.servercontroller.GameController;
import it.polimi.ingsw.server.servercontroller.SocketManager;
import it.polimi.ingsw.server.servercontroller.controllerstates.RunningGameState;
import it.polimi.ingsw.server.servercontroller.controllerstates.ServerInitState;
import it.polimi.ingsw.server.servercontroller.exceptions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements InterfaceServer {

    private static int portRMI;
    private static int portTCP;
    private final Map<String,InterfaceClient> clientMapRMI = new ConcurrentHashMap<>();
    private final GameController controller = GameController.getGameController(this);
    private static final int CHECK_DELAY_MILLISECONDS = 5000;


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

    @Override
    public void presentation(InterfaceClient cl, String nickname) throws RemoteException {
        try {
            switch(controller.presentation(nickname)) {
                case 0 -> {  // you're joining but I need another nickname
                    cl.askForNewNickname();
                }
                case 1 -> { //joined a "new" game
                    clientMapRMI.put(nickname,cl);
                    cl.confirmConnection(false);
                }
                case 2 ->{ //first player joining a "restored" game
                    clientMapRMI.put(nickname,cl);
                    cl.lobbyCreated(false);
                }
                case 3 -> { //joining a restored game
                    clientMapRMI.put(nickname,cl);
                    cl.confirmConnection(true);
                }
            }
        } catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
            clientMapRMI.put(nickname,cl);
            controller.disconnectAllUsers();
        } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
            clientMapRMI.put(nickname,cl);
            controller.startGame();
            controller.yourTarget();
            cl.startClearThread();
            controller.updateView();
        } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
            cl.disconnectUser(1);
        } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
            clientMapRMI.put(nickname,cl);
            cl.askParameters();
        } catch (WaitForLobbyParametersException e) {
            cl.waitForLobbyCreation();
        }
    }

    @Override
    public void sendParameters(InterfaceClient cl,int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        if(controller.createLobby(maxPlayerNumber,onlyOneCommonCard))
            cl.lobbyCreated(true);
        else
            cl.askParametersAgain();
    }

    public void executeMove(Body move) throws RemoteException {
        try {
            controller.executeMove(move);
            controller.updateView();
        } catch (InvalidMoveException e) {
            clientMapRMI.get(move.getPlayerNickname()).incorrectMove();
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

    public void sendCardsRMI(List<String> commonTargetCardList) throws RemoteException {
        for(Player p : controller.getPlayerList()) {
            if(clientMapRMI.containsKey(p.getNickname())) {
                clientMapRMI.get(p.getNickname()).receiveCards(p.getPersonalTargetCard().getPersonalNumber(),commonTargetCardList);
            }
        }
    }

    public boolean checkReceiver(String nickname) {
        return clientMapRMI.containsKey(nickname);
    }

    @Override
    public void peerToPeerMsgHandler(String sender, String receiver, String text, String localDateTime) throws RemoteException {
        try {
            controller.peerToPeerMsg(sender,receiver,text, localDateTime);
        } catch (InvalidNicknameException e) {
            clientMapRMI.get(sender).wrongMessageWarning(text);
        }
    }

    public void peerToPeerMsg(String sender, String receiver, String text, String localDateTime) throws RemoteException {
        clientMapRMI.get(receiver).receiveMessage(sender, text, localDateTime);
    }

    @Override
    public void broadcastMsgHandler(String sender, String text, String localDateTime) throws RemoteException {
        controller.broadcastMsg(sender,text, localDateTime);
    }

    @Override
    public void voluntaryDisconnection(String nickname) throws RemoteException {
        controller.changePlayerConnectionStatus(nickname);
        clientMapRMI.remove(nickname); //stops the ping from the server towards that user
        if(controller.getActivePlayer().getNickname().equals(nickname)){
            controller.changeActivePlayer();
            controller.updateView();
        }
    }

    public void broadcastMsg(String sender, String text,String localDateTime) throws RemoteException {
        for(InterfaceClient interfaceClient: clientMapRMI.values()) {
            interfaceClient.receiveMessage(sender, text,localDateTime );
        }
    }

    @Override
    public void clearRMI() throws RemoteException { //ping called from client to server
        //it's empty, we need to check on the other side for RemoteExceptions
    }
    public void startCheckThreadRMI() {
        new Thread(() -> {
            while (true) {
                try {
                    for (String nickname : clientMapRMI.keySet()) {
                        System.out.println("Pingo " + nickname);
                        try {
                            clientMapRMI.get(nickname).check();
                        } catch (RemoteException e) {
                            System.out.println("Client " + nickname + " disconnesso");
                            controller.changePlayerConnectionStatus(nickname);
                            if(controller.getState().getClass().equals(RunningGameState.class)
                                && controller.getActivePlayer().getNickname().equals(nickname)){
                                controller.changeActivePlayer();
                                controller.updateView();
                            }
                            if(controller.getPlayerList().size()==1 && controller.getMaxPlayerNumber()==0){
                                controller.changeState(new ServerInitState());
                                controller.getPlayerList().clear();
                            }
                            clientMapRMI.remove(nickname);
                            break;
                        }
                        Thread.sleep(CHECK_DELAY_MILLISECONDS);
                    }
                } catch (InterruptedException | RemoteException e) {
                    //thread interrupted
                }
            }
        }).start();
    }

    public void disconnectLastRMIUser() {
        for(String nickname: clientMapRMI.keySet()) {
            for(Player player: controller.getPlayerList()) {
                if(player.isConnected() && player.getNickname().equals(nickname)){
                    try {
                        clientMapRMI.get(nickname).disconnectUser(9); //todo qua manca in realtà la funzione in grafica
                        clientMapRMI.clear();
                    } catch (RemoteException ignored) {
                    }
                }
            }

        }
    }

    public void rejoinRequest(String nickname, InterfaceClient cl) throws RemoteException{
        if(controller.checkReJoinRequest(nickname)){
            if(controller.didLastUserMadeHisMove()){ //updateView for everyone as soon as he connects because he needs to make a move right away
                controller.setLastUserMadeHisMove(false);
                clientMapRMI.put(nickname, cl);
                clientMapRMI.get(nickname).rejoinedMatch();
                controller.changeActivePlayer();
                controller.updateView();
            }
            else { //he'll wait for the first updateView
                clientMapRMI.put(nickname, cl);
                clientMapRMI.get(nickname).rejoinedMatch();
            }
        }
        else{ //impossible case
            clientMapRMI.get(nickname).invalidPlayerForRejoiningTheMatch();
        }
    }
}
