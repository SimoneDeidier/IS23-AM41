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

    @Override
    public void run() {
        launch();
    }

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

    @Override
    public void setClientController(ClientController clientController) {
        GraphicUserInterface.clientController = clientController;
    }

    @Override
    public void getGameParameters() {
        loginScreenController.getGameParameters();
    }

    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
        clientController.startClearThread();
    }

    @Override
    public void invalidNickname() {
        loginScreenController.invalidNickname();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
         clientController.sendParameters(numPlayers, numCommons);
    }

    @Override
    public void nicknameAccepted(int nPlayers, List<String> lobby) {
        loginScreenController.nicknameAccepted(nPlayers, lobby);
    }

    @Override
    public void lobbyCreated(int nPlayers, Map<String, Boolean> lobby) {
        loginScreenController.lobbyCreated(nPlayers, lobby);
    }

    @Override
    public void waitForLobby() {
        loginScreenController.waitForLobby();
    }

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

    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        gameScreenController.addMessageInChat(message, sender, localDateTime);
    }

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

    @Override
    public void exit() {
        clientController.exit();
        guiStage.close();
    }

    @Override
    public void rejoinedMatch() {
        loginScreenController.rejoinedMatch();
    }

    @Override
    public void invalidPlayer() {
        loginScreenController.invalidPlayerNickname();
    }

    @Override
    public boolean isYourTurn() {
        return isYourTurn;
    }

    @Override
    public void sendMove(int col) {
        clientController.sendMove(col);
    }

    @Override
    public void insertInPositionPicked(int[] el) {
        clientController.insertInPositionPicked(el);
    }
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

    @Override
    public void swapColsTUI(int col1, int col2) {
        // NO IN GUI
    }

    @Override
    public void incorrectMove() {
        gameScreenController.incorrectMove();
    }

    @Override
    public void wrongReceiver() {
        gameScreenController.wrongReceiver();
    }

    @Override
    public void wrongParameters() {
        loginScreenController.wrongParameters();
    }

    @Override
    public boolean columnHasEnoughSpace(int col) {
        return clientController.columnHasEnoughSpace(col);
    }

    @Override
    public void removeInPositionPicked(int col) {
        clientController.removeInPositionPicked(col);
    }

    @Override
    public void playerRestored() {
        loginScreenController.playerRestored();
    }

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

    @Override
    public void lobbyRestored() {
        loginScreenController.lobbyRestored();
    }

    @Override
    public void fullLobby() {
        loginScreenController.fullLobby();
    }

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

    @Override
    public void playerDisconnected(String nickname) {
        if(loadedGame) {
            gameScreenController.playerDisconnected(nickname);
        }
    }

    @Override
    public void playerReconnected(String nickname) {
        if(loadedGame) {
            gameScreenController.playerReconnected(nickname);
        }
    }

    @Override
    public void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers) {
        Platform.runLater(() -> {
            if(yourTurn && !waitForOtherPlayers) {
                gameScreenController.setTakeableItems(takeableItems);
            }
        });
    }

    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        clientController.exitWithoutWaitingDisconnectFromServer();
        guiStage.close();
    }

    @Override
    public void userConnected(String nickname) {
        loginScreenController.userConnected(nickname);
    }

    @Override
    public void disconnectedFromLobby(String nickname) {
        loginScreenController.disconnectedFromLobby(nickname);
    }

}
