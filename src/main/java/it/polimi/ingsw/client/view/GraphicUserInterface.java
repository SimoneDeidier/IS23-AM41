package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.EndGameScreenController;
import it.polimi.ingsw.client.view.controllers.GameScreenController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import it.polimi.ingsw.messages.NewView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphicUserInterface extends Application implements UserInterface, Serializable {


    private static ClientController clientController;
    private static Stage guiStage;
    private static LoginScreenController loginScreenController;
    private static GameScreenController gameScreenController;
    private boolean isYourTurn = false;
    private boolean loadedGame = false;

    /**
     * Starting method to run the UI
     */
    @Override
    public void run() {
        launch();
    }

    /**
     * Starting method to run the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {
        GraphicUserInterface.guiStage = stage;
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
        guiStage.setScene(new Scene(loader.load()));
        loginScreenController = loader.getController();
        loginScreenController.setGui(this);
        guiStage.setResizable(false);
        guiStage.setTitle("Welcome to My Shelfie!");
        guiStage.setOnCloseRequest(e -> {
            e.consume();
            loginScreenController.exit(this);
        });
        guiStage.show();
    }

    /**
     * Set the clientController
     * @param clientController provide the clientController to be set
     */
    @Override
    public void setClientController(ClientController clientController) {
        GraphicUserInterface.clientController = clientController;
    }

    /**
     * Ask the first user for the game parameters: number of players and number of common target cards
     */
    @Override
    public void getGameParameters() {
        loginScreenController.getGameParameters();
    }

    /**
     * Send the nickname to the client controller
     * @param nickname the nickname to send to the client controller
     */
    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
        clientController.startClearThread();
    }

    /**
     * Warn the user that the name is already in use
     */
    @Override
    public void invalidNickname() {
        loginScreenController.invalidNickname();
    }

    /**
     * Send the parameters to the client controller
     * @param numPlayers the number of players
     * @param numCommons the number of common target cards
     */
    @Override
    public void sendParameters(int numPlayers, int numCommons) {
         clientController.sendParameters(numPlayers, numCommons);
    }

    /**
     * Tells the user that the nickname has been accepted and that he's in the lobby
     */
    @Override
    public void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby) {
        loginScreenController.nicknameAccepted(nPlayers, lobby);
    }

    /**
     * Tells the user that a lobby was created and that the game will start soon.
     */
    @Override
    public void lobbyCreated(int nPlayers, Map<String, Boolean> lobby) {
        loginScreenController.lobbyCreated(nPlayers, lobby);
    }

    /**
     * Tells the user that someone else is trying to create a lobby and to retry in a few seconds
     */
    @Override
    public void waitForLobby() {
        loginScreenController.waitForLobby();
    }

    /**
     * Save data useful for the game to start and load the game ui
     * @param personalTargetCardNumber the number of the personal target card picked for the user
     * @param nickname the nickname of the user
     * @param commonTargetGoals list of strings with the common target goals
     */
    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {
        Platform.runLater(() -> {
            guiStage.close();
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/GameScreen.fxml"));
            try {
                guiStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            gameScreenController = loader.getController();
            gameScreenController.setGui(this);
            try {
                gameScreenController.setPersonalTargetCard(personalTargetCardNumber);
            } catch (URISyntaxException | IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            try {
                gameScreenController.setCommonTargetCard(commonTargetGoals);
            }
            catch (URISyntaxException | FileNotFoundException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            try {
                gameScreenController.setupPlayerShelf();
            } catch (URISyntaxException | FileNotFoundException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            guiStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
                final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.ESCAPE);

                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyCombination.match(keyEvent)) {
                        gameScreenController.openMenu();
                    }
                }
            });
            guiStage.setOnCloseRequest(e -> {
                e.consume();
                gameScreenController.openMenu();
            });
            guiStage.setResizable(false);;
            guiStage.setTitle("My Shelfie - Gaming Phase");
            guiStage.show();
            loadedGame = true;
        });
    }

    /**
     * Send a message from the chat to the client controller
     * @param message message to send to the client controller
     */
    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    /**
     * Receive a message from the chat and display it in the chat
     * @param message message received
     * @param sender nickname of the person that sent the message
     * @param localDateTime date and time of when the message was sent
     */
    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        gameScreenController.addMessageInChat(message, sender, localDateTime);
    }

    /**
     * Receive all the data necessary to update the text user interface
     * @param newView NewView object containing all the updated data for the interface
     */
    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        String playerNickname = clientController.getPlayerNickname();
        this.isYourTurn = Objects.equals(newView.getActivePlayer(), playerNickname) && !newView.youAreTheLastUserAndYouAlreadyMadeYourMove();
        Platform.runLater(() -> {
            try {
                if(!newView.isGameOver()) {
                    if(Objects.equals(newView.getPlayerList().get(0), playerNickname)) {
                        gameScreenController.setChair();
                    }
                    if(!newView.getEndGameToken().isTakeable()) {
                        gameScreenController.removeEndGameToken();
                    }
                    if(Objects.equals(newView.getEndGameToken().getTakenBy(), playerNickname)) {
                        gameScreenController.setEndGameToken(newView.getEndGameToken());
                    }
                    gameScreenController.clearBoard();
                    gameScreenController.setBoardItems(newView.getBoardItems(), newView.getBoardBitMask());
                    gameScreenController.setPlayerText(playerNickname, newView.getNicknameToPointsMap().get(playerNickname));
                    gameScreenController.setPersonalShelf(newView.getNicknameToShelfMap().get(playerNickname));
                    gameScreenController.setTokens(newView.getCommonsToTokens(), newView.getPlayersToTokens().get(playerNickname));
                    gameScreenController.setOtherPlayersParameters(newView.getNicknameToShelfMap(), newView.getNicknameToPointsMap(), playerNickname, newView.getPlayerList().get(0));
                    gameScreenController.setYourTurnPane(this.isYourTurn, newView.getActivePlayer(), newView.youAreTheLastUserAndYouAlreadyMadeYourMove());
                    if(newView.youAreTheLastUserAndYouAlreadyMadeYourMove()) {
                        gameScreenController.waitForOtherPlayers();
                    }
                }
                else {
                    guiStage.close();
                    FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/EndGameScreen.fxml"));
                    guiStage.setScene(new Scene(loader.load()));
                    EndGameScreenController endGameScreenController = loader.getController();
                    endGameScreenController.setGui(this);
                    endGameScreenController.setParameters(newView);
                    guiStage.setTitle("My Shelfie - End game!");
                    guiStage.setResizable(false);
                    guiStage.show();
                }
            } catch (URISyntaxException | IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
        });
    }

    public GraphicUserInterface getGui() {
        return this;
    }

    /**
     * Calls the exit method from the client controller to end the game
     */
    @Override
    public void exit() {
        clientController.exit();
        guiStage.close();
    }

    /**
     * Tells the user that he rejoined a game he left before
     */
    @Override
    public void rejoinedMatch() {
        loginScreenController.rejoinedMatch();
    }

    /**
     * Warns the user that he tried to send a message to a non existent user
     */
    @Override
    public void invalidPlayer() {
        loginScreenController.invalidPlayerNickname();
    }

    /**
     * returns the isYourTurn parameter that determines if it is the user turn or not
     * @return boolean referring to the user turn
     */
    @Override
    public boolean isYourTurn() {
        return isYourTurn;
    }

    /**
     * Send the column where to put the selected items for the move to the client controller
     * @param col column to send to the client controller
     */
    @Override
    public void sendMove(int col) {
        clientController.sendMove(col);
    }

    /**
     * Send to the controller the coordinates of the element chosen by the user
     * @param el coordinates of the element picked
     */
    @Override
    public void insertInPositionPicked(int[] el) {
        clientController.insertInPositionPicked(el);
    }

    /**
     * Checks if the user has already picked the maximum amount of items for the move
     */
    @Override
    public int getPositionPickedSize() {
        return clientController.getPositionPickedSize();
    }

    @Override
    public void swapColsGUI(List<Node> list) {
        clientController.swapCols(list);
    }

    @Override
    public int getSwapColIndex(Node n) {
        return gameScreenController.getSwapColIndex(n);
    }

    /**
     * Swap the order of two selected items
     * @param col1 first column to swap
     * @param col2 second column to swap
     */
    @Override
    public void swapColsTUI(int col1, int col2) {
        // NO IN GUI
    }

    /**
     * Warns the user that his move is incorrect and that the board has been reset to the previous state
     */
    @Override
    public void incorrectMove() {
        gameScreenController.incorrectMove();
    }

    /**
     * Warns the user that he tried to send a personal message to a non-existent user
     */
    @Override
    public void wrongReceiver() {
        gameScreenController.wrongReceiver();
    }

    /**
     * Warns the user that he provided parameters that are not acceptable for the game
     */
    @Override
    public void wrongParameters() {
        loginScreenController.wrongParameters();
    }

    /**
     * Send the column selected to the client controller to check if there's enough space to insert the items the user selected
     * @param col column to check
     * @return boolean telling if the column has enough space or not
     */
    @Override
    public boolean columnHasEnoughSpace(int col) {
        return clientController.columnHasEnoughSpace(col);
    }

    /**
     * Send to the controller the column of the selected items that the user wants to remove
     * @param col column where to remove the item
     */
    @Override
    public void removeInPositionPicked(int col) {
        clientController.removeInPositionPicked(col);
    }

    /**
     * Tells the user that he's been restored from a previous saved game and that he's now waiting in the lobby
     */
    @Override
    public void playerRestored() {
        loginScreenController.playerRestored();
    }

    /**
     * Warns the user that the server is not responding
     */
    @Override
    public void serverNotResponding() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/ServerNotResponding.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            stage.setTitle("My Shelfie - Server not responding!");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            guiStage.close();
            clientController.closeConnection();
        });
    }

    /**
     * Tells the user that the lobby has been restored.
     */
    @Override
    public void lobbyRestored() {
        loginScreenController.lobbyRestored();
    }

    /**
     * Warns the user that the lobby is full, and to close the client and try again
     */
    @Override
    public void fullLobby() {
        loginScreenController.fullLobby();
    }

    /**
     * Warns the user that the lobby cannot be restored because a player that wasn't in the lobby tried to join
     */
    @Override
    public void cantRestoreLobby() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/CantRestoreLobby.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            stage.setResizable(false);
            stage.setTitle("My Shelfie - Can't restore lobby!");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            guiStage.close();
        });
    }

    /**
     * Congratulate the user for the win because all the other players left for too much time
     */
    @Override
    public void alonePlayerWins() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/AlonePlayerWins.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            stage.setResizable(false);
            stage.setTitle("My Shelfie - You have won!!!");
            stage.showAndWait();
            guiStage.close();
        });
    }

    /**
     * Tells the user that a user disconnected from the game
     * @param nickname nickname of the user that disconnected
     */
    @Override
    public void playerDisconnected(String nickname) {
        if(loadedGame) {
            gameScreenController.playerDisconnected(nickname);
        }
    }

    /**
     * Tells the user that a user reconnected to the game
     * @param nickname nickname of the user that reconnected
     */
    @Override
    public void playerReconnected(String nickname) {
        if(loadedGame) {
            gameScreenController.playerReconnected(nickname);
        }
    }

    /**
     * Register which parameters are takeable on the board
     * @param takeableItems a bitmask of the takeable items on the board
     * @param yourTurn a boolean telling if it is the user turn or not
     * @param waitForOtherPlayers a boolean about other players turns
     */
    @Override
    public void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers) {
        Platform.runLater(() -> {
            if(yourTurn && !waitForOtherPlayers) {
                gameScreenController.setTakeableItems(takeableItems);
            }
        });
    }

    /**
     * Calls the client controller method to exit from the game without disconnecting from the server
     */
    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        clientController.exitWithoutWaitingDisconnectFromServer();
        guiStage.close();
    }

    /**
     * Show in the GUI the name of the new player connected.
     *
     * @param nickname
     */
    @Override
    public void userConnected(String nickname) {
        loginScreenController.userConnected(nickname);
    }

    /**
     * Show in the GUI the name of the disconnected player as: "nickname - DISCONNECTED!"
     *
     * @param nickname
     */
    @Override
    public void disconnectedFromLobby(String nickname) {
        loginScreenController.disconnectedFromLobby(nickname);
    }

    /**
     * Show in the GUI the name of the player reconnected.
     * @param nickname
     */
    @Override
    public void userRejoined(String nickname) {
        loginScreenController.userRejoined(nickname);
    }

}
