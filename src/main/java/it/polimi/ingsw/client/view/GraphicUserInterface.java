package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.view.controllers.EndGameScreenController;
import it.polimi.ingsw.client.view.controllers.GameScreenController;
import it.polimi.ingsw.client.view.controllers.LoginScreenController;
import it.polimi.ingsw.client.view.controllers.MenuController;
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
import javafx.stage.Stage;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class GraphicUserInterface extends Application implements UserInterface, Serializable {

    private static ClientController clientController;
    private static Stage guiStage;
    private static LoginScreenController loginScreenController;
    private static GameScreenController gameScreenController;
    private boolean isYourTurn = false;

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
    public void nicknameAccepted() {
        loginScreenController.nicknameAccepted();
    }

    @Override
    public void lobbyCreated() {
        loginScreenController.lobbyCreated();
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
                e.printStackTrace();
            }
            gameScreenController = loader.getController();
            gameScreenController.setGui(this);
            try {
                gameScreenController.setPersonalTargetCard(personalTargetCardNumber);
            } catch (URISyntaxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                gameScreenController.setCommonTargetCard(commonTargetGoals);
            }
            catch (URISyntaxException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                gameScreenController.setupPlayerShelf();
            } catch (URISyntaxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            guiStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
                final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.ESCAPE);

                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyCombination.match(keyEvent)) {
                        Platform.runLater(() -> {
                            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/Menu.fxml"));
                            Stage stage = new Stage();
                            try {
                                stage.setScene(new Scene(loader.load()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            MenuController menuController = loader.getController();
                            menuController.setGui(getGui());
                            menuController.setMenuStage(stage);
                            stage.setResizable(false);
                            stage.setTitle("My Shelfie - Main menu!");
                            stage.show();
                        });
                    }
                }
            });
            guiStage.setResizable(false);;
            guiStage.setTitle("My Shelfie - Gaming Phase");
            guiStage.show();
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
        this.isYourTurn = Objects.equals(newView.getActivePlayer(), playerNickname);
        Platform.runLater(() -> {
            try {
                if(!newView.isGameOver()) {
                    gameScreenController.clearBoard();
                    gameScreenController.setBoardItems(newView.getBoardItems(), newView.getBoardBitMask());
                    gameScreenController.setPlayerText(playerNickname, newView.getNicknameToPointsMap().get(playerNickname));
                    gameScreenController.setPersonalShelf(newView.getNicknameToShelfMap().get(clientController.getPlayerNickname()));
                    gameScreenController.setOtherPlayersParameters(newView.getNicknameToShelfMap(), newView.getNicknameToPointsMap(), clientController.getPlayerNickname());
                    gameScreenController.setYourTurnPane(this.isYourTurn);
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
                e.printStackTrace();
            }
        });
    }

    public GraphicUserInterface getGui() {
        return this;
    }

    @Override
    public void disconnect() {
        clientController.disconnect();
        Platform.runLater(() -> {
            guiStage.close();
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LoginScreen.fxml"));
            guiStage = new Stage();
            try {
                guiStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loginScreenController = loader.getController();
            loginScreenController.setGui(this);
            loginScreenController.rejoinScreen();
            guiStage.setResizable(false);
            guiStage.setTitle("Welcome to My Shelfie!");
            guiStage.show();
        });
    }

    @Override
    public void rejoinMatch() {
        clientController.rejoinMatch();
    }

    @Override
    public void exit() {
        clientController.disconnect();
        guiStage.close();
    }

    @Override
    public void rejoinedMatch() {
        System.out.println("Called rejoined in gui");
        // loginScreenController.rejoinedMatch();
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
    public void swapCols(List<Node> list) {
        clientController.swapCols(list);
    }

    @Override
    public int getSwapColIndex(Node n) {
        return gameScreenController.getSwapColIndex(n);
    }

    @Override
    public void swapCols(int col1, int col2) {
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

}
