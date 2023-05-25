package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.servercontroller.GameController;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserInterface {

    void run();
    void setClientController(ClientController clientController);
    void getGameParameters();

    void sendNickname(String nickname);

    void invalidNickname();

    void sendParameters(int numPlayers, int numCommons);

    void nicknameAccepted();

    void lobbyCreated();

    void waitForLobby();

    void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals);

    void sendMessage(String message);

    void receiveMessage(String message, String sender, String localDateTime);

    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;

    void disconnect();

    void rejoinMatch();

    void exit();

    void rejoinedMatch();

    void invalidPlayer();

    boolean isYourTurn();

    void sendMove(int col);

    void insertInPositionPicked(int[] el);

    int getPositionPickedSize();

    void swapCols(List<Node> list);

    int getSwapColIndex(Node n);

    void swapCols(int col1, int col2);

    void incorrectMove();

    void wrongReceiver();

    void wrongParameters();
}
