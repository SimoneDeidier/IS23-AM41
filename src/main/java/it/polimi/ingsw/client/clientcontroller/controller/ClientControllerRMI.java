package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.items.Item;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ClientControllerRMI implements ClientController, Serializable {

    private final ConnectionRMI connectionRMI;
    private UserInterface userInterface = null;
    private String playerNickname;
    private int personalTargetCardNumber;
    private List<String> commonGoalNameList = new ArrayList<>();

    public ClientControllerRMI(ConnectionRMI connectionRMI) {
        this.connectionRMI = connectionRMI;
    }

    @Override
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        try {
            connectionRMI.presentation(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getParameters() {
        userInterface.getGameParameters();
    }

    @Override
    public void invalidNickname() {
        userInterface.invalidNickname();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        try {
            connectionRMI.sendParameters(numPlayers,numCommons==1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nicknameAccepted() {
        userInterface.nicknameAccepted();
    }

    @Override
    public void lobbyCreated() {
        userInterface.lobbyCreated();
    }

    @Override
    public void waitForLobby() {
        userInterface.waitForLobby();
    }

    @Override
    public void setPersonalTargetCardNumber(int personalTargetCardNumber) {
        this.personalTargetCardNumber=personalTargetCardNumber;
    }

    @Override
    public void setCommonGoalList(List<String> commonGoalNameList) {
        this.commonGoalNameList = commonGoalNameList;
    }

    @Override
    public void loadGameScreen() throws IOException {
        userInterface.loadGameScreen(personalTargetCardNumber, playerNickname, this.commonGoalNameList);
    }

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
            connectionRMI.sendPrivateMessage(body);
        }
        else {
            connectionRMI.sendBroadcastMessage(body);
        }
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        userInterface.receiveMessage(message, sender, localDateTime);
    }

    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        userInterface.updateView(newView);
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void rejoinMatch() {

    }

    @Override
    public void rejoinedMatch() {

    }

    @Override
    public void invalidPlayer() {

    }

    @Override
    public void insertInPositionPicked(int[] el) {

    }

    @Override
    public int getPositionPickedSize() {
        return 0;
    }

    @Override
    public void sendMove(int col) {

    }

    @Override
    public void swapCols(List<Node> list) {

    }

    @Override
    public void swapCols(int col1, int col2) {

    }

    @Override
    public void incorrectMove() {

    }

}
