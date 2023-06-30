package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.messages.NewView;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface ClientController {

    /**
     * Starts the user interface based on the specified UI type.
     * @param uiType The type of user interface to start ("gui" for graphic user interface, "tui" for text user interface).
     */
    void startUserInterface(String uiType);
    /**
     * Sends the player's nickname to the server.
     * @param nickname The player's nickname.
     */
    void sendNickname(String nickname);
    /**
     * Requests the game parameters from the user interface.
     */
    void getParameters();
    /**
     * Notifies the user that the entered nickname is invalid.
     */
    void invalidNickname();
    /**
     * Sends the game parameters to the server.
     * @param numPlayers The number of players.
     * @param numCommons The number of common goals.
     */
    void sendParameters(int numPlayers, int numCommons);
    /**
     * Notifies the user that the entered nickname is accepted.
     */
    void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby);
    /**
     * Notifies the user that the lobby is created.
     */
    void lobbyCreated(int nPlayers, Map<String, Boolean> lobby);
    /**
     * Notifies the user to wait for lobby.
     */
    void waitForLobby();
    /**
     * Sets the personal target card number.
     *
     * @param personalTargetCardNumber The personal target card number.
     */
    void setPersonalTargetCardNumber(int personalTargetCardNumber);
    /**
     * Sets the list of common goals.
     *
     * @param commonGoalsNameList The list of common goal names.
     */
    void setCommonGoalList(List<String> commonGoalsNameList);
    /**
     * Loads the game screen.
     *
     * @throws IOException If an I/O error occurs.
     */
    void loadGameScreen() throws IOException;
    /**
     * Sends a message (private or broadcast) via the server.
     *
     * @param message The message to send.
     */
    void sendMessage(String message);
    /**
     * Receives a message from the server and passes it to the user interface to display.
     *
     * @param message the message received from the server
     * @param sender the sender of the message
     * @param localDateTime the timestamp of the message
     */
    void receiveMessage(String message, String sender, String localDateTime);
    /**
     * Updates the view based on the new view received from the server.
     *
     * @param newView the updated view received from the server
     * @throws FileNotFoundException if a file required by the view is not found
     * @throws URISyntaxException if there is a syntax error in a URI
     */
    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;
    /**
     * Returns the nickname of the player.
     *
     * @return the player's nickname
     */
    String getPlayerNickname();
    /**
     * Notifies the user that the player has rejoined the match.
     */
    void rejoinedMatch();
    /**
     * Notifies the user that the player is invalid.
     * This occurs when the rejoin attempt fails.
     */
    void invalidPlayer();
    /**
     * Inserts the specified element into the positionPicked list.
     *
     * @param el the element to be inserted
     */
    void insertInPositionPicked(int[] el);
    /**
     * Returns the size of the positionPicked list.
     *
     * @return the size of the positionPicked list
     */
    int getPositionPickedSize();
    /**
     * Sends a correct move to the server based on the selected column, otherwise it notifies user about the incorrect move.
     *
     * @param col the selected column
     */
    void sendMove(int col);
    /**
     * Swaps the columns in the positionPicked list based on the specified nodes.
     *
     * @param list the list of nodes representing the columns to be swapped
     */
    void swapCols(List<Node> list);
    /**
     *Swaps the columns in the positionPicked list based on the specified columns.
     *
     * @param col1 the index of the first column to be swapped
     * @param col2 the index of the second column to be swapped
     */
    void swapCols(int col1, int col2);
    /**
     * Notifies the user that the user gave an incorrect move.
     */
    void incorrectMove();
    /**
     * Notifies the user that the receiver of a message is misspelled.
     */
    void wrongReceiver();
    /**
     * Notifies the user that the parameters provided are incorrect.
     */
    void wrongParameters();
    /**
     * Checks if the specified column has enough space to accommodate the picked positions.
     *
     * @param col the index of the column to check
     * @return true if the column has enough space, false otherwise
     */
    boolean columnHasEnoughSpace(int col);
    /**
     * Removes the position at the specified column from the positionPicked list.
     *
     * @param col the index of the column to remove
     */
    void removeInPositionPicked(int col);
    /**
     * Notifies the user that the player has been restored.
     */
    void playerRestored();
    /**
     * Starts the clear thread.
     */
    void startClearThread();
    /**
     * Notifies the user that the server is not responding.
     */
    void serverNotResponding();
    /**
     * Closes the connection and that also stops the ping thread.
     */
    void closeConnection();
    /**
     * Notifies the user that the lobby has been restored.
     */
    void lobbyRestored();
    /**
     * Exits the game: stops the ping thread and initiates a voluntary disconnection.
     */
    void exit();
    /**
     * Notifies the user that the lobby is full.
     */
    void fullLobby();
    /**
     * Notifies the user that the lobby cannot be restored.
     *
     * @throws IOException if an I/O error occurs
     */
    void cantRestoreLobby() throws IOException;
    /**
     * Notifies the user that the last player remained wins the game.
     */
    void alonePlayerWins();
    /**
     * Notifies the user that a player has disconnected.
     *
     * @param nickname the nickname of the disconnected player
     */
    void playerDisconnected(String nickname);
    /**
     * Notifies the user that a player has reconnected.
     *
     * @param nickname the nickname of the reconnected player
     */
    void playerReconnected(String nickname);
    /**
     * Exits the game without waiting for a disconnection from the server.
     */
    void exitWithoutWaitingDisconnectFromServer();
    /**
     * Notify the user that another user has connected
     *
     * @param playerNickname nickname of the player
     */
    void userConnected(String playerNickname);
    /**
     * Notify the user that a user has disconnected from the lobby
     *
     * @param playerNickname nickname of the player
     */
    void disconnectedFromLobby(String playerNickname);
    /**
     * Notify the user that another user has rejoined the game
     *
     * @param playerNickname nickname of the user
     */
    void userRejoined(String playerNickname);
}
