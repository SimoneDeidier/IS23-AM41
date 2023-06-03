package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.items.Item;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ClientControllerRMI implements ClientController, Serializable {
    //todo per ogni remoteException o interrupted non tirare throw new RuntimeException(e); ma messaggio d'errore così
    //se caduta rete non errore brutto ma bello?

    private final ConnectionRMI connectionRMI;
    private UserInterface userInterface = null;
    private String playerNickname;
    private int personalTargetCardNumber;
    private List<String> commonGoalNameList = new ArrayList<>();
    private List<int[]> positionPicked = new ArrayList<>(3);
    private Map<Integer, Integer> columnsToFreeSpaces = new HashMap<>(5);
    private final static int ROWS = 6;
    private final static int COLS = 5;
    private final static int BOARD_DIM = 9;
    private boolean[][] takeableItems = new boolean[BOARD_DIM][BOARD_DIM];

    public ClientControllerRMI(ConnectionRMI connectionRMI) {
        this.connectionRMI = connectionRMI;
    }

    @Override
    public void startUserInterface(String uiType) {
        switch (uiType) {
            case "gui" -> userInterface = new GraphicUserInterface();
            case "tui" ->  userInterface = new TextUserInterface();
        }
        userInterface.setClientController(this);
        Thread userInterfaceThread = new Thread(() -> userInterface.run());
        userInterfaceThread.start();
        try {
            userInterfaceThread.join();
            System.err.println("JOINED THE VIEW THREAD");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        try {
            connectionRMI.presentation(nickname);
        } catch (RemoteException e) {
            System.out.println("Network error, we're sorry for the inconvenience, try restarting the client");
        }
    }

    @Override
    public void getParameters() {
        userInterface.getGameParameters();
    }

    @Override
    public void invalidNickname() {
        userInterface.invalidNickname();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        try {
            connectionRMI.sendParameters(numPlayers,numCommons==1);
        } catch (RemoteException e) {
            System.out.println("Network error, we're sorry for the inconvenience, try restarting the client");
        }
    }

    @Override
    public void nicknameAccepted() {
        userInterface.nicknameAccepted();
    }

    @Override
    public void lobbyCreated() {
        userInterface.lobbyCreated();
    }

    @Override
    public void waitForLobby() {
        userInterface.waitForLobby();
    }

    @Override
    public void setPersonalTargetCardNumber(int personalTargetCardNumber) {
        this.personalTargetCardNumber=personalTargetCardNumber;
    }

    @Override
    public void setCommonGoalList(List<String> commonGoalNameList) {
        this.commonGoalNameList = commonGoalNameList;
    }

    @Override
    public void loadGameScreen() throws IOException {
        userInterface.loadGameScreen(personalTargetCardNumber, playerNickname, this.commonGoalNameList);
    }

    @Override
    public void sendMessage(String message) {
        Body body = new Body();
        body.setText(message);
        body.setSenderNickname(playerNickname);
        LocalDateTime time = LocalDateTime.now();
        body.setLocalDateTime(time.truncatedTo(ChronoUnit.SECONDS).toString());
        String[] words = message.split(" ");
        if(words[0].startsWith("@")){
            body.setReceiverNickname(words[0].substring(1));
            connectionRMI.sendPrivateMessage(body);
        }
        else {
            connectionRMI.sendBroadcastMessage(body);
        }
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        userInterface.receiveMessage(message, sender, localDateTime);
    }

    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        for(int i = 0; i < COLS; i++) {
            int count = 0;
            for(int j = 0; j < ROWS; j++) {
                if(newView.getNicknameToShelfMap().get(playerNickname)[j][i] == null) {
                    count++;
                }
            }
            columnsToFreeSpaces.put(i, count);
        }
        Item[][] board = newView.getBoardItems();
        for(int i = 0; i < BOARD_DIM; i++) {
            for(int j = 0; j < BOARD_DIM; j++) {
                if(board[i][j] != null) {
                    if(i == 0 || i == BOARD_DIM - 1 || j == 0 || j == BOARD_DIM - 1) {
                        takeableItems[i][j] = true;
                    }
                    else if(board[i-1][j] == null || board[i+1][j] == null || board[i][j-1] == null || board[i][j+1] == null) {
                        takeableItems[i][j] = true;
                    }
                    else takeableItems[i][j] = false;
                }
                else takeableItems[i][j] = false;
            }
        }
        userInterface.updateView(newView);
        userInterface.setTakeableItems(takeableItems);
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void rejoinedMatch() {
        System.out.println("Called rejoined in controller");
        try { //restarting the ping to the server
            connectionRMI.setClientConnected(true);
            connectionRMI.startClearThread();
        } catch (RemoteException e) {
            System.out.println("Network error, we're sorry for the inconvenience, try restarting the client");;
        }
        userInterface.rejoinedMatch();
    }

    @Override
    public void invalidPlayer() { //todo, è quando fallisce il re join
        userInterface.invalidPlayer();
    }

    @Override
    public void insertInPositionPicked(int[] el) {
        positionPicked.add(el);
    }

    @Override
    public int getPositionPickedSize() {
        return positionPicked.size();
    }

    @Override
    public void sendMove(int col) {
        Body body = new Body();
        body.setColumn(col);
        body.setPositionsPicked(positionPicked);
        body.setPlayerNickname(playerNickname);
        connectionRMI.sendMoveToServer(body);
        positionPicked = new ArrayList<>(3);
    }

    @Override
    public void swapCols(List<Node> list) {
        int col1 = userInterface.getSwapColIndex(list.get(0));
        int col2 = userInterface.getSwapColIndex(list.get(1));
        Collections.swap(positionPicked, col1, col2);
    }

    @Override
    public void swapCols(int col1, int col2) {
        Collections.swap(positionPicked, col1, col2);
    }

    @Override
    public void incorrectMove() {
        userInterface.incorrectMove();
    }

    @Override
    public void wrongReceiver() {
        userInterface.wrongReceiver();
    }

    @Override
    public void wrongParameters() {
        userInterface.wrongParameters();
    }

    @Override
    public boolean columnHasEnoughSpace(int col) {
        return columnsToFreeSpaces.get(col) >= positionPicked.size();
    }

    @Override
    public void removeInPositionPicked(int col) {
        positionPicked.remove(col);
    }

    @Override
    public void playerRestored() {
        userInterface.playerRestored();
    }

    @Override
    public void startClearThread() { //only used in TCP
    }

    @Override
    public void serverNotResponding() {
        connectionRMI.setClientConnected(false); //stops the ping thread
        userInterface.serverNotResponding();
    }

    @Override
    public void closeConnection() { //todo capire se serve altro
        connectionRMI.setClientConnected(false); //stops the ping thread
    }

    @Override
    public void lobbyRestored() {
        userInterface.lobbyRestored();
    }

    @Override
    public void exit() {
        connectionRMI.setClientConnected(false); //stops the ping thread
        connectionRMI.voluntaryDisconnection();
    }

    @Override
    public void fullLobby() {
        userInterface.fullLobby();
    }

    @Override
    public void cantRestoreLobby() throws IOException {
        userInterface.cantRestoreLobby();
    }

    @Override
    public void alonePlayerWins() {
        userInterface.alonePlayerWins();
    }

    @Override
    public void playerDisconnected(String nickname) {
        userInterface.playerDisconnected(nickname);
    }

    @Override
    public void playerReconnected(String nickname) {
        userInterface.playerReconnected(nickname);
    }

}
