package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.Connection;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.items.Item;
import javafx.fxml.FXML;
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

    void disconnect();

    void rejoinMatch();

    void rejoinedMatch();

    void invalidPlayer();

    int getPickedItemListSize();

    void insertInPickedItemList(Item i);

    void insertInPositionPicked(int[] el);

    void sendMove(int col);
}
