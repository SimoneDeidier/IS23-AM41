package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.TCPMessageController;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.items.Item;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ClientControllerTCP implements ClientController {

    private final TCPMessageController tcpMessageController;
    private UserInterface userInterface = null;
    private String playerNickname;
    private int personalTargetCardNumber;
    private List<String> commonGoalNameList;
    private List<int[]> positionPicked = new ArrayList<>(3);
    private Map<Integer, Integer> columnsToFreeSpaces = new HashMap<>(5);
    private final static int ROWS = 6;
    private final static int COLS = 5;
    private final static int BOARD_DIM = 9;
    private boolean[][] takeableItems = new boolean[BOARD_DIM][BOARD_DIM];
    private boolean[][] bitmask;
    private Item[][] boardMatrix;

    public ClientControllerTCP(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }

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
        }
        catch (InterruptedException e) {
            System.err.println("The UI thread could not be closed, please kill the task and restart the client!");
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        Body body = new Body();
        body.setPlayerNickname(nickname);
        tcpMessageController.printTCPMessage("Presentation", body);
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
        Body body = new Body();
        body.setPlayerNickname(this.playerNickname);
        body.setNumberOfPlayers(numPlayers);
        body.setOnlyOneCommon(numCommons == 1);
        tcpMessageController.printTCPMessage("Create Lobby", body);
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
        this.personalTargetCardNumber = personalTargetCardNumber;
    }

    @Override
    public void setCommonGoalList(List<String> commonGoalNameList) {
        this.commonGoalNameList = commonGoalNameList;
    }

    @Override
    public void loadGameScreen() throws IOException {
        userInterface.loadGameScreen(personalTargetCardNumber, playerNickname, commonGoalNameList);
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
            tcpMessageController.printTCPMessage("Peer-to-Peer Msg", body);
        }
        else {
            tcpMessageController.printTCPMessage("Broadcast Msg", body);
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
        bitmask= newView.getBoardBitMask();
        boardMatrix= newView.getBoardItems();
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
        boolean yourTurn = Objects.equals(newView.getActivePlayer(), playerNickname);
        userInterface.setTakeableItems(takeableItems, yourTurn, newView.youAreTheLastUserAndYouAlreadyMadeYourMove());
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void rejoinedMatch() {
        userInterface.rejoinedMatch();
    }

    @Override
    public void invalidPlayer() {
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
        int size = positionPicked.size();
        if(size < 1 || size>3) {
            positionPicked= new ArrayList<>();
            userInterface.incorrectMove();
            return;
        }
        //Check if all the elements have a free side
        for(int[] i : positionPicked){
            if(!hasFreeSide(i[0],i[1])){
                positionPicked= new ArrayList<>();
                userInterface.incorrectMove();
                return;
            }
        }
        //Check if the elements are in line
        if(!checkInLine(positionPicked)){
            positionPicked= new ArrayList<>();
            userInterface.incorrectMove();
            return;
        }
        Body body = new Body();
        body.setColumn(col);
        body.setPositionsPicked(positionPicked);
        body.setPlayerNickname(playerNickname);
        tcpMessageController.printTCPMessage("Move", body);
        positionPicked = new ArrayList<>(3);
    }

    public boolean hasFreeSide(int i,int j){
        //Check up
        if(i>0 && getBoardMatrixElement(i-1,j)==null){
            return true;
        }
        //Check down
        if(i<BOARD_DIM-1 && getBoardMatrixElement(i+1,j)==null){
            return true;
        }
        //Check left
        if(j>0 && getBoardMatrixElement(i,j-1)==null){
            return true;
        }
        //Check right
        return j<BOARD_DIM-1 && getBoardMatrixElement(i, j + 1) == null;
    }

    public Item getBoardMatrixElement(int i, int j){
        return boardMatrix[i][j];
    }

    public boolean checkInLine(List<int[]> list){
        int x1=list.get(0)[0],y1=list.get(0)[1];
        if(list.size()==2){
            int x2=list.get(1)[0],y2=list.get(1)[1];
            return (x1 == x2 - 1 && y1 == y2) || (x1 == x2 + 1 && y1 == y2)
                    || (x1 == x2 && y1 == y2 - 1) || ((x1 == x2 && y1 == y2 + 1));
        }

        if(list.size()==3){
            int x2=list.get(1)[0],y2=list.get(1)[1],x3=list.get(2)[0],y3=list.get(2)[1];
            if(x1 == (x2 + 1) && (x3==x2-1 || x3==x1+1) && y1==y2 && y2==y3){
                return true;
            }
            if(x1 == (x2 - 1) && (x3==x1-1 || x3==x2+1) && y1==y2 && y2==y3){
                return true;
            }
            if(y1 == (y2 + 1) && (y3==y2-1 || y3==y1+1) && x1==x2 && x2==x3){
                return true;
            }
            if(y1 == (y2 - 1) && (y3 == y2 + 1 || y3 == y1 - 1) && x1 == x2 && x2 == x3)
                return true;
            if(x1==x2+2 &&  x1==x3+1 && y1==y2 && y2==y3)
                return true;
            if(x1== (x2-2) && x1==x3-1 && y1==y2 && y2==y3)
                return true;
            if(y1 == (y2+2) && y1 == y3+1 && x1==x2 && x2==x3)
                return true;
            if(y1 == y2-2 && y1 == y3-1 && x1==x2 && x2==x3)
                return true;
            return false;
        }
        return true; //case where list.size==1
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
        for(int[] el : positionPicked) {
            System.out.println(el[0] + " - " + el[1]);
        }
    }

    @Override
    public void playerRestored() {
        userInterface.playerRestored();
    }

    @Override
    public void startClearThread() {
        tcpMessageController.startClearThread();
    }

    @Override
    public void serverNotResponding() {
        userInterface.serverNotResponding();
    }

    @Override
    public void closeConnection() {
        tcpMessageController.closeConnection();
    }

    @Override
    public void lobbyRestored() {
        userInterface.lobbyRestored();
    }

    @Override
    public void exit() {
        tcpMessageController.stopClearThread();
        tcpMessageController.printTCPMessage("Disconnect", null);
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

    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        tcpMessageController.stopClearThread();
        tcpMessageController.closeClient();
    }

}
