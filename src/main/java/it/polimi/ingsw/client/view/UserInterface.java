package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface UserInterface extends Runnable {

    void run();
    void setClientController(ClientController clientController);
    void getGameParameters();

    void sendNickname(String nickname);

    void invalidNickname();

    void sendParameters(int numPlayers, int numCommons);

    void nicknameAccepted(int nPlayers, List<String> lobby);

    void lobbyCreated(int nPlayers, List<String> lobby);

    void waitForLobby();

    void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals);

    void sendMessage(String message);

    void receiveMessage(String message, String sender, String localDateTime);

    void updateView(NewView newView) throws FileNotFoundException, URISyntaxException;

    void exit();

    void rejoinedMatch();

    void invalidPlayer();

    boolean isYourTurn();

    void sendMove(int col);

    void insertInPositionPicked(int[] el);

    int getPositionPickedSize();

    void swapColsGUI(List<Node> list); //todo Perchè in interfaccia se sono una per ognuna delle due grafiche? (Prima era metodo overloaded ma secondo me comunque non ha senso)
    void swapColsTUI(int col1, int col2);

    int getSwapColIndex(Node n);

    void incorrectMove();

    void wrongReceiver();

    void wrongParameters();

    boolean columnHasEnoughSpace(int col);

    void removeInPositionPicked(int col);

    void playerRestored();

    void serverNotResponding();

    void lobbyRestored();

    void fullLobby();

    void cantRestoreLobby() throws IOException;

    void alonePlayerWins();

    void playerDisconnected(String nickname);

    void playerReconnected(String nickname);

    void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers);

    void exitWithoutWaitingDisconnectFromServer();
}
