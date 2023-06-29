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
/**
 * The ClientControllerTCP class represents the TCP implementation of the client controller.
 * It handles the communication between the client and the server using TCP protocol.
 * It also interacts with the user interface to display information and receive user input.
 */
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

    /**
     * Constructor for the ClientControllerTCP class.
     *
     * @param tcpMessageController the TCPMessageController used for communication with the server
     */
    public ClientControllerTCP(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }
    /**
     * Starts the user interface based on the given UI type.
     *
     * @param uiType the type of user interface ("gui" for GraphicUserInterface, "tui" for TextUserInterface)
     */
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
    /**
     * Sends the player's nickname to the server.
     *
     * @param nickname the nickname of the player
     */
    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        Body body = new Body();
        body.setPlayerNickname(nickname);
        tcpMessageController.printTCPMessage("Presentation", body);
    }
    /**
     * Asks the game parameters from the user interface.
     */
    @Override
    public void getParameters() {
        userInterface.getGameParameters();
    }
    /**
     * Notifies the user that the nickname entered by the player is invalid.
     */
    @Override
    public void invalidNickname() {
        userInterface.invalidNickname();
    }
    /**
     * Sends the game parameters to the server.
     *
     * @param numPlayers the number of players
     * @param numCommons the number of common goals
     */
    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        Body body = new Body();
        body.setPlayerNickname(this.playerNickname);
        body.setNumberOfPlayers(numPlayers);
        body.setOnlyOneCommon(numCommons == 1);
        tcpMessageController.printTCPMessage("Create Lobby", body);
    }

    /**
     * Notifies the user that the nickname provided was accepted.
     */
    @Override
    public void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby) {
        userInterface.nicknameAccepted(nPlayers, lobby);
    }
    /**
     * Notifies the user that the lobby was created.
     */
    @Override
    public void lobbyCreated(int nPlayers, Map<String, Boolean> lobby) {
        userInterface.lobbyCreated(nPlayers, lobby);
    }
    /**
     * Notifies the user that to wait for the lobby to start.
     */
    @Override
    public void waitForLobby() {
        userInterface.waitForLobby();
    }

    /**
     * Sets player's personal target card number.
     *
     * @param personalTargetCardNumber the personal target card number.
     */
    @Override
    public void setPersonalTargetCardNumber(int personalTargetCardNumber) {
        this.personalTargetCardNumber = personalTargetCardNumber;
    }

    /**
     * Sets the common target cards list.
     * @param commonGoalNameList list of the names of the common target cards.
     */
    @Override
    public void setCommonGoalList(List<String> commonGoalNameList) {
        this.commonGoalNameList = commonGoalNameList;
    }

    /**
     * Display game screen to the user interface to display.
     *
     * @throws IOException if an I/O exception happens.
     */
    @Override
    public void loadGameScreen() throws IOException {
        userInterface.loadGameScreen(personalTargetCardNumber, playerNickname, commonGoalNameList);
    }

    /**
     * Sends a message (private or broadcast) from TCP player via the server.
     *
     * @param message The message to send.
     */
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
    /**
     * Receives a message from the server and passes it to the user interface to display.
     *
     * @param message the message received from the server
     * @param sender the sender of the message
     * @param localDateTime the timestamp of the message
     */
    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        userInterface.receiveMessage(message, sender, localDateTime);
    }
    /**
     * Updates the view based on the new view received from the server.
     *
     * @param newView the updated view received from the server
     * @throws FileNotFoundException if a file required by the view is not found
     * @throws URISyntaxException if there is a syntax error in a URI
     */
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
    /**
     * Returns the nickname of the player.
     *
     * @return the player's nickname
     */
    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
    /**
     * Notifies the user that the player has rejoined the match.
     */
    @Override
    public void rejoinedMatch() {
        userInterface.rejoinedMatch();
    }
    /**
     * Notifies the user that the player is invalid.
     * This occurs when the rejoin attempt fails.
     */
    @Override
    public void invalidPlayer() {
        userInterface.invalidPlayer();
    }
    /**
     * Inserts the specified element into the positionPicked list.
     *
     * @param el the element to be inserted
     */
    @Override
    public void insertInPositionPicked(int[] el) {
        positionPicked.add(el);
    }
    /**
     * Returns the size of the positionPicked list.
     *
     * @return the size of the positionPicked list
     */
    @Override
    public int getPositionPickedSize() {
        return positionPicked.size();
    }
    /**
     * Sends a correct move to the server based on the selected column, otherwise it notifies user about the incorrect move.
     *
     * @param col the selected column
     */
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
    /**
     * Checks if the specified position has a free side.
     *
     * @param i the row index of the position
     * @param j the column index of the position
     * @return true if the position has a free side, false otherwise
     */
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
    /**
     * Returns the element at the specified position in the boardMatrix.
     *
     * @param i the row index of the position
     * @param j the column index of the position
     * @return the element at the specified position in the boardMatrix
     */
    public Item getBoardMatrixElement(int i, int j){
        return boardMatrix[i][j];
    }
    /**
     * Checks if the elements in the list are in line.
     *
     * @param list the list of elements to be checked
     * @return true if the elements are in line, false otherwise
     */
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
    /**
     * Swaps the columns in the positionPicked list based on the specified nodes.
     *
     * @param list the list of nodes representing the columns to be swapped
     */
    @Override
    public void swapCols(List<Node> list) {
        int col1 = userInterface.getSwapColIndex(list.get(0));
        int col2 = userInterface.getSwapColIndex(list.get(1));
        Collections.swap(positionPicked, col1, col2);
    }
    /**
     *Swaps the columns in the positionPicked list based on the specified columns.
     *
     * @param col1 the index of the first column to be swapped
     * @param col2 the index of the second column to be swapped
     */
    @Override
    public void swapCols(int col1, int col2) {
        Collections.swap(positionPicked, col1, col2);
    }
    /**
     * Notifies the user that the user gave an incorrect move.
     */
    @Override
    public void incorrectMove() {
        userInterface.incorrectMove();
    }
    /**
     * Notifies the user that the receiver of a message is misspelled.
     */
    @Override
    public void wrongReceiver() {
        userInterface.wrongReceiver();
    }
    /**
     * Notifies the user that the parameters provided are incorrect.
     */
    @Override
    public void wrongParameters() {
        userInterface.wrongParameters();
    }
    /**
     * Checks if the specified column has enough space to accommodate the picked positions.
     *
     * @param col the index of the column to check
     * @return true if the column has enough space, false otherwise
     */
    @Override
    public boolean columnHasEnoughSpace(int col) {
        return columnsToFreeSpaces.get(col) >= positionPicked.size();
    }
    /**
     * Removes the position at the specified column from the positionPicked list.
     *
     * @param col the index of the column to remove
     */
    @Override
    public void removeInPositionPicked(int col) {
        positionPicked.remove(col);
    }
    /**
     * Notifies the user that the player has been restored.
     */
    @Override
    public void playerRestored() {
        userInterface.playerRestored();
    }
    /**
     * Starts the clear thread. (Only used in TCP)
     */
    @Override
    public void startClearThread() {
        tcpMessageController.startClearThread();
    }
    /**
     * Notifies the user that the server is not responding.
     */
    @Override
    public void serverNotResponding() {
        userInterface.serverNotResponding();
    }
    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        tcpMessageController.closeConnection();
    }
    /**
     * Notifies the user that the lobby has been restored.
     */
    @Override
    public void lobbyRestored() {
        userInterface.lobbyRestored();
    }
    /**
     * Exits the game: stops the ping thread.
     */
    @Override
    public void exit() {
        tcpMessageController.stopClearThread();
        tcpMessageController.printTCPMessage("Disconnect", null);
    }
    /**
     * Notifies the user that the lobby is full.
     */
    @Override
    public void fullLobby() {
        userInterface.fullLobby();
    }
    /**
     * Notifies the user that the lobby cannot be restored.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void cantRestoreLobby() throws IOException {
        userInterface.cantRestoreLobby();
    }
    /**
     * Notifies the user that the last player remained wins the game.
     */
    @Override
    public void alonePlayerWins() {
        userInterface.alonePlayerWins();
    }
    /**
     * Notifies the user that a player has disconnected.
     *
     * @param nickname the nickname of the disconnected player
     */
    @Override
    public void playerDisconnected(String nickname) {
        userInterface.playerDisconnected(nickname);
    }
    /**
     * Notifies the user that a player has reconnected.
     *
     * @param nickname the nickname of the reconnected player
     */
    @Override
    public void playerReconnected(String nickname) {
       userInterface.playerReconnected(nickname);
    }
    /**
     * Exits the game without waiting for a disconnection from the server: stops the ping thread and close the client.
     */
    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        tcpMessageController.stopClearThread();
        tcpMessageController.closeClient();
    }

    /**
     * Notifies the user of the new connected player.
     * @param playerNickname
     */
    @Override
    public void userConnected(String playerNickname) {
        userInterface.userConnected(playerNickname);
    }

    /**
     * Notifies the user of the disconnected player.
     * @param playerNickname
     */
    @Override
    public void disconnectedFromLobby(String playerNickname) {
        userInterface.disconnectedFromLobby(playerNickname);
    }

    /**
     * Notifies the user of the reconnected player.
     * @param playerNickname
     */
    @Override
    public void userRejoined(String playerNickname) {
        userInterface.userRejoined(playerNickname);
    }
}
