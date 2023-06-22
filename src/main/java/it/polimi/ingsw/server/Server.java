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
import it.polimi.ingsw.server.servercontroller.controllerstates.WaitingForPlayerState;
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
    private static String ipAddress;
    private final Map<String,InterfaceClient> clientMapRMI = new ConcurrentHashMap<>();
    private final GameController controller = GameController.getGameController(this);
    private static final int CHECK_DELAY_MILLISECONDS = 5000;


    public static void main(String[] args) {

        // SET THE PORT NUMBER
        if(!parsePortNumber(args)) {
            portRMI = setPortNumber("RMI", 0);
            portTCP = setPortNumber("TCP", portRMI);
        }

        if(!parseIpAddress(args)) {
            ipAddress = setIpAddress();
        }

        // START RMI SERVER
        InterfaceServer stub;
        Server obj =  new Server();

        System.setProperty("java.rmi.server.hostname", ipAddress.toLowerCase());

        try {
            stub = (InterfaceServer) UnicastRemoteObject.exportObject(obj, portRMI);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return;
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(portRMI);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            registry.bind("InterfaceServer", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.err.println("RMI server ready - listening on port " + portRMI + ".");

        // START TCP SERVER
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            try (ServerSocket serverSocket = new ServerSocket(portTCP)) {
                System.err.println("TCP Server ready - listening on port " + portTCP + ".");
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        executor.submit(new SocketManager(socket));
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        break;
                    }
                }
                System.out.println("Server socket was closed - server shutting down.");
                executor.shutdown();
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
        System.out.println("All threads are joined - server shutting down.");

    }

    public static boolean parsePortNumber(String[] args) {

        boolean rmiOK = false;
        boolean tcpOK = false;

        for(int i = 0; i < args.length - 1; i++) {
            if((Objects.equals(args[i], "-r") || Objects.equals(args[i], "--rmi")) && args[i+1].matches("[0-9]+")) {
                int parsed = Integer.parseInt(args[i+1]);
                if(parsed <= 1024 || parsed > 65535 || (tcpOK && (parsed == portTCP))) {
                    System.out.println("Invalid port number.");
                    return false;
                }
                portRMI = parsed;
                rmiOK = true;
            }
            if((Objects.equals(args[i], "-t") || Objects.equals(args[i], "--tcp")) && args[i+1].matches("[0-9]+")) {
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

    public static boolean parseIpAddress(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i + 1];
            if(Objects.equals(cmd, "--ipaddr") && (par.matches("[0-9][0-9.]*[0-9]+") || par.equalsIgnoreCase("localhost"))) {
                ipAddress = par;
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

    public static String setIpAddress() {
        String input;
        Scanner in = new Scanner(System.in);

        do {
            System.out.println("Please insert the IP address of the server: ");
            input = in.nextLine();
        } while(!input.matches("[0-9][0-9.]*[0-9]+") && !input.equalsIgnoreCase("localhost"));

        return input;
    }

    @Override
    public void presentation(InterfaceClient cl, String nickname) throws IOException {
        try {
            switch(controller.presentation(nickname)) {
                case 0 -> {  // you're joining but I need another nickname
                    cl.askForNewNickname();
                }
                case 1 -> { //joined a "new" game
                    clientMapRMI.put(nickname,cl); //starts the ping towards that user
                    int nPlayers = controller.getMaxPlayerNumber();
                    List<String> lobby = new ArrayList<>();
                    for(Player p : controller.getPlayerList()) {
                        lobby.add(p.getNickname());
                    }
                    cl.confirmConnection(false, nPlayers, lobby);
                    controller.notifyOfConnectedUser(nickname);
                }
                case 2 ->{ //first player joining a "restored" game
                    clientMapRMI.put(nickname,cl); //starts the ping towards that user
                    int nPlayers = controller.getMaxPlayerNumber();
                    List<String> lobby = new ArrayList<>();
                    for(Player p : controller.getPlayerList()) {
                        lobby.add(p.getNickname());
                    }
                    cl.lobbyCreated(false, nPlayers, lobby);
                }
                case 3 -> { //joining a restored game
                    clientMapRMI.put(nickname,cl); //starts the ping towards that user
                    int nPlayers = controller.getMaxPlayerNumber();
                    List<String> lobby = new ArrayList<>();
                    for(Player p : controller.getPlayerList()) {
                        lobby.add(p.getNickname());
                    }
                    cl.confirmConnection(true, nPlayers, lobby);
                }
            }
        } catch (CancelGameException e) { //the game is being canceled because a restoring of a saved game failed
            clientMapRMI.put(nickname,cl); //starts the ping towards that user
            controller.disconnectAllUsers();
            controller.prepareForNewGame();
        } catch (GameStartException e) { //the game is starting because everyone is connected, updating everyone views
            clientMapRMI.put(nickname,cl); //starts the ping towards that user
            controller.startGame();
            controller.yourTarget();
            try {
                cl.startClearThread();
            }catch (RemoteException ignored){
            }
            controller.updateView();
        } catch (FullLobbyException e) { //you can't connect right now, the lobby is full or a game is already playing on the server
            cl.fullLobby();
        } catch (FirstPlayerException e) { //you're the first player connecting for creating a new game, I need more parameters from you
            clientMapRMI.put(nickname,cl); //starts the ping towards that user
            cl.askParameters();
        } catch (WaitForLobbyParametersException e) {
            cl.waitForLobbyCreation();
        } catch (RejoinRequestException e) {
            rejoinRequest(nickname,cl);
        }
    }

    public void rejoinRequest(String nickname, InterfaceClient cl){
        try {
            controller.changePlayerConnectionStatus(nickname);
            if (controller.didLastUserMadeHisMove()) { //updateView for everyone as soon as he connects because he needs to make a move right away
                controller.notifyOfReconnectionAllUsers(nickname);
                controller.setLastConnectedUserMadeHisMove(false);
                clientMapRMI.put(nickname, cl);
                try {
                    clientMapRMI.get(nickname).rejoinedMatch();
                }catch(RemoteException ignored){
                }
                sendCardsRMI();
                controller.changeActivePlayer();
                controller.updateView();
            } else { //he'll wait for the first updateView
                controller.notifyOfReconnectionAllUsers(nickname);
                clientMapRMI.put(nickname, cl);
                clientMapRMI.get(nickname).rejoinedMatch();
                sendCardsRMI();
            }
        }catch (RemoteException ignored){
        }
    }

    @Override
    public void sendParameters(InterfaceClient cl,int maxPlayerNumber, boolean onlyOneCommonCard) throws RemoteException {
        if(controller.createLobby(maxPlayerNumber,onlyOneCommonCard)) {
            int nPlayers = controller.getMaxPlayerNumber();
            List<String> lobby = new ArrayList<>();
            for(Player p : controller.getPlayerList()) {
                lobby.add(p.getNickname());
            }
            cl.lobbyCreated(true, nPlayers, lobby);
        }
        else {
            cl.askParametersAgain();
        }
    }

    @Override
    public void executeMove(Body move) throws RemoteException {
        try {
            controller.executeMove(move);
            controller.updateView();
        } catch (InvalidMoveException e) {
            clientMapRMI.get(move.getPlayerNickname()).incorrectMove();
        }
    }

    public void updateViewRMI(NewView newView) {
        for(InterfaceClient cl: clientMapRMI.values()){
            try {
                cl.updateView(newView);
            } catch (RemoteException ignored) {

            }
        }
    }



    public void disconnectAllRMIUsers() {
        for(Map.Entry<String,InterfaceClient> entry : clientMapRMI.entrySet()){
            try {
                entry.getValue().disconnectUser(0);
            } catch (RemoteException ignored) {
            }
            clientMapRMI.remove(entry.getKey());
        }
    }

    public void sendCardsRMI() {
        List<String> commonList=new ArrayList<>();
        for(CommonTargetCard commonTargetCard: controller.getCommonTargetCardsList()){
            commonList.add(commonTargetCard.getName());
        }
        for(Player p : controller.getPlayerList()) {
            if(clientMapRMI.containsKey(p.getNickname())) {
                try {
                    clientMapRMI.get(p.getNickname()).receiveCards(p.getPersonalTargetCard().getPersonalNumber(),commonList);
                } catch (RemoteException ignored) {
                }
            }
        }
    }

    public boolean checkReceiverInRMI(String nickname) {
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

    public void peerToPeerMsg(String sender, String receiver, String text, String localDateTime) {
        try {
            clientMapRMI.get(receiver).receiveMessage(sender, text, localDateTime);
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public void broadcastMsgHandler(String sender, String text, String localDateTime) throws RemoteException {
        controller.broadcastMsg(sender,text, localDateTime);
    }

    public void broadcastMsg(String sender, String text,String localDateTime) {
        for(InterfaceClient interfaceClient: clientMapRMI.values()) {
            try {
                interfaceClient.receiveMessage(sender, text,localDateTime );
            } catch (RemoteException ignored) {
            }
        }
    }

    @Override
    public void voluntaryDisconnection(String nickname) throws RemoteException {
        controller.notifyOfDisconnectionAllUsers(nickname);
        controller.changePlayerConnectionStatus(nickname);
        clientMapRMI.remove(nickname); //stops the ping from the server towards that user
        if(controller.getActivePlayer().getNickname().equals(nickname)){
            controller.changeActivePlayer();
            controller.updateView();
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
                        } catch (RemoteException | NullPointerException e) {
                            System.out.println("Client " + nickname + " disconnesso");
                            controller.notifyOfDisconnectionAllUsers(nickname);
                            controller.changePlayerConnectionStatus(nickname);
                            if (controller.getState().getClass().equals(RunningGameState.class)
                                    && controller.getActivePlayer().getNickname().equals(nickname)) {
                                controller.changeActivePlayer();
                                clientMapRMI.remove(nickname);
                                controller.updateView();
                                break;
                            }
                            if (controller.getPlayerList().size() == 1 && controller.getMaxPlayerNumber() == 0) { //the first player who was setting up the lobby disconnected
                                controller.changeState(new ServerInitState());
                                controller.getPlayerList().clear();
                            }
                            clientMapRMI.remove(nickname);
                            if(controller.getState().getClass().equals(WaitingForPlayerState.class)) {
                                controller.notifyOfDisconnectionFromLobby(nickname);
                            }
                            break;
                        }
                        Thread.sleep(CHECK_DELAY_MILLISECONDS);
                    }
                } catch(InterruptedException | RemoteException e){
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
                        clientMapRMI.get(nickname).disconnectUser(1);
                    } catch (RemoteException ignored) {
                    }
                }
            }

        }
    }

    public void prepareServerForNewGame(){
        clientMapRMI.clear();
    }

    public void notifyOfDisconnectionAllRMIUsers(String nickname) {
        for(String user:clientMapRMI.keySet()){
            try {
                clientMapRMI.get(user).notificationForDisconnection(nickname);
            } catch (RemoteException ignored) {
            }
        }
    }

    public void notifyOfReconnectionAllRMIUsers(String nickname) {
        for(String user: clientMapRMI.keySet()){
            try {
                clientMapRMI.get(user).notificationForReconnection(nickname);
            } catch (RemoteException ignored) {
            }
        }
    }

    public void notifyOfConnectedUser(String nickname) {
        for(String s : clientMapRMI.keySet()) {
            if(!Objects.equals(s, nickname)) {
                clientMapRMI.get(s).notifyConnectedUser(nickname);
            }
        }
    }

    public void notifyOfDisconnectionFromLobby(String nickname) {
        for(String s : clientMapRMI.keySet()) {
            clientMapRMI.get(s).disconnectedFromLobby(nickname);
        }
    }
}
