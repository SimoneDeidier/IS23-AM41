package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface UserInterface extends Runnable {
    /**
     * Starting method to run the UI
     */
    void run();

    /**
     * Set the clientController
     * @param clientController provide the clientController to be set
     */
    void setClientController(ClientController clientController);

    /**
     * Ask the first user for the game parameters: number of players and number of common target cards
     */
    void getGameParameters();

    /**
     * Send the nickname to the client controller
     * @param nickname the nickname to send to the client controller
     */
    void sendNickname(String nickname);

    /**
     * Warn the user that the name is already in use
     */
    void invalidNickname();

    /**
     * Send the parameters to the client controller
     * @param numPlayers the number of players
     * @param numCommons the number of common target cards
     */
    void sendParameters(int numPlayers, int numCommons);
  
    /**
     * Tells the user that the nickname has been accepted and that he's in the lobby
     */
    void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby);
  
    /**
     * Tells the user that a lobby was created and that the game will start soon.
     */
    void lobbyCreated(int nPlayers, Map<String, Boolean> lobby);

    /**
     * Tells the user that someone else is trying to create a lobby and to retry in a few seconds
     */
    void waitForLobby();

    /**
     * Save data useful for the game to start
     * @param personalTargetCardNumber the number of the personal target card picked for the user
     * @param nickname the nickname of the user
     * @param commonTargetGoals list of strings with the common target goals
     */
    void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals);

    /**
     * Send a message from the chat to the client controller
     * @param message message to send to the client controller
     */
    void sendMessage(String message);

    /**
     * Receive a message from the chat and display it in the chat
     * @param message message received
     * @param sender nickname of the person that sent the message
     * @param localDateTime date and time of when the message was sent
     */
    void receiveMessage(String message, String sender, String localDateTime);

    /**
     * Receive all the data necessary to update the text user interface
     * @param newView NewView object containing all the updated data for the interface
     */
    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;

    /**
     * Calls the exit method from the client controller to end the game
     */
    void exit();

    /**
     * Tells the user that he rejoined a game he left before
     */
    void rejoinedMatch();

    /**
     * Warns the user that he tried to send a message to a non existent user
     */
    void invalidPlayer();

    /**
     * returns the isYourTurn parameter that determines if it is the user turn or not
     * @return boolean referring to the user turn
     */
    boolean isYourTurn();

    /**
     * Send the column where to put the selected items for the move to the client controller
     * @param col column to send to the client controller
     */
    void sendMove(int col);

    /**
     * Send to the controller the coordinates of the element chosen by the user
     * @param el coordinates of the element picked
     */
    void insertInPositionPicked(int[] el);

    /**
     * Checks if the user has already picked the maximum amount of items for the move
     */
    int getPositionPickedSize();

    void swapColsGUI(List<Node> list); //todo Perchè in interfaccia se sono una per ognuna delle due grafiche? (Prima era metodo overloaded ma secondo me comunque non ha senso)

    /**
     * Swap the order of two selected items
     * @param col1 first column to swap
     * @param col2 second column to swap
     */
    void swapColsTUI(int col1, int col2);

    int getSwapColIndex(Node n);

    /**
     * Warns the user that his move is incorrect and that the board has been reset to the previous state
     */
    void incorrectMove();

    /**
     * Warns the user that he tried to send a personal message to a non-existent user
     */
    void wrongReceiver();

    /**
     * Warns the user that he provided parameters that are not acceptable for the game
     */
    void wrongParameters();

    /**
     * Send the column selected to the client controller to check if there's enough space to insert the items the user selected
     * @param col column to check
     * @return boolean telling if the column has enough space or not
     */
    boolean columnHasEnoughSpace(int col);

    /**
     * Send to the controller the column of the selected items that the user wants to remove
     * @param col column where to remove the item
     */
    void removeInPositionPicked(int col);

    /**
     * Tells the user that he's been restored from a previous saved game and that he's now waiting in the lobby
     */
    void playerRestored();

    /**
     * Warns the user that the server is not responding
     */
    void serverNotResponding();

    /**
     * Tells the user that the lobby has been restored.
     */
    void lobbyRestored();

    /**
     * Warns the user that the lobby is full, and to close the client and try again
     */
    void fullLobby();

    /**
     * Warns the user that the lobby cannot be restored because a player that wasn't in the lobby tried to join
     */
    void cantRestoreLobby() throws IOException;

    /**
     * Congratulate the user for the win because all the other players left for too much time
     */
    void alonePlayerWins();

    /**
     * Tells the user that a user disconnected from the game
     * @param nickname nickname of the user that disconnected
     */
    void playerDisconnected(String nickname);

    /**
     * Tells the user that a user reconnected to the game
     * @param nickname nickname of the user that reconnected
     */
    void playerReconnected(String nickname);

    /**
     * Register which parameters are takeable on the board
     * @param takeableItems a bitmask of the takeable items on the board
     * @param yourTurn a boolean telling if it is the user turn or not
     * @param waitForOtherPlayers a boolean about other players turns
     */
    void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers);

    /**
     * Calls the client controller method to exit from the game without disconnecting from the server
     */
    void exitWithoutWaitingDisconnectFromServer();

    void userConnected(String nickname);

    void disconnectedFromLobby(String nickname);

    void userRejoined(String nickname);
}
