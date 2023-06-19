package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.Connection;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.items.Item;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public interface ClientController {

    void startUserInterface(String uiType);
    void sendNickname(String nickname);
    void getParameters();
    void invalidNickname();
    void sendParameters(int numPlayers, int numCommons);
    void nicknameAccepted();
    void lobbyCreated();
    void waitForLobby();
    void setPersonalTargetCardNumber(int personalTargetCardNumber);
    void  setCommonGoalList(List<String> commonGoalsNameList);
    void loadGameScreen() throws IOException;
    void sendMessage(String message);
    void receiveMessage(String message, String sender, String localDateTime);

    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;

    String getPlayerNickname();

    void rejoinedMatch();

    void invalidPlayer();

    void insertInPositionPicked(int[] el);

    int getPositionPickedSize();

    void sendMove(int col);

    void swapCols(List<Node> list);

    void swapCols(int col1, int col2);

    void incorrectMove();

    void wrongReceiver();

    void wrongParameters();

    boolean columnHasEnoughSpace(int col);

    void removeInPositionPicked(int col);

    void playerRestored();

    void startClearThread();

    void serverNotResponding();

    void closeConnection();

    void lobbyRestored();

    void exit();

    void fullLobby();

    void cantRestoreLobby() throws IOException;

    void alonePlayerWins();

    void playerDisconnected(String nickname);

    void playerReconnected(String nickane);

    void exitWithoutWaitingDisconnectFromServer();
}
