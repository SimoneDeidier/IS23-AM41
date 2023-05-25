package it.polimi.ingsw.client.clientcontroller.controller;

import it.polimi.ingsw.client.clientcontroller.TCPMessageController;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.NewView;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientControllerTCP implements ClientController {

    private final TCPMessageController tcpMessageController;
    private UserInterface userInterface = null;
    private String playerNickname;
    private int personalTargetCardNumber;
    private List<String> commonGoalNameList;
    private List<int[]> positionPicked = new ArrayList<>(3);

    public ClientControllerTCP(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }

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
            System.err.println("JOINED THE GUI THREAD");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNickname(String nickname) {
        this.playerNickname = nickname;
        Body body = new Body();
        body.setPlayerNickname(nickname);
        tcpMessageController.printTCPMessage("Presentation", body);
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
        Body body = new Body();
        body.setPlayerNickname(this.playerNickname);
        body.setNumberOfPlayers(numPlayers);
        body.setOnlyOneCommon(numCommons == 1);
        tcpMessageController.printTCPMessage("Create Lobby", body);
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
        this.personalTargetCardNumber = personalTargetCardNumber;
    }

    @Override
    public void setCommonGoalList(List<String> commonGoalNameList) {
        this.commonGoalNameList = commonGoalNameList;
    }

    @Override
    public void loadGameScreen() throws IOException {
        userInterface.loadGameScreen(personalTargetCardNumber, playerNickname, commonGoalNameList);
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
            tcpMessageController.printTCPMessage("Peer-to-Peer Msg", body);
        }
        else {
            tcpMessageController.printTCPMessage("Broadcast Msg", body);
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
        tcpMessageController.printTCPMessage("Disconnect", null);
    }

    @Override
    public void rejoinMatch() {
        tcpMessageController.rejoinMatch();
    }

    @Override
    public void rejoinedMatch() {
        System.out.println("Called rejoined in controller");
        userInterface.rejoinedMatch();
    }

    @Override
    public void invalidPlayer() {
        userInterface.invalidPlayer();
    }

    @Override
    public void insertInPositionPicked(int[] el) {
        positionPicked.add(el);
    }
    @Override
    public int getPositionPickedSize() {
        return positionPicked.size();
    }

    @Override
    public void sendMove(int col) {
        Body body = new Body();
        body.setColumn(col);
        body.setPositionsPicked(positionPicked);
        body.setPlayerNickname(playerNickname);
        tcpMessageController.printTCPMessage("Move", body);
        positionPicked = new ArrayList<>(3);
    }

    @Override
    public void swapCols(List<Node> list) {
        int col1 = userInterface.getSwapColIndex(list.get(0));
        int col2 = userInterface.getSwapColIndex(list.get(1));
        Collections.swap(positionPicked, col1, col2);
    }

    @Override
    public void swapCols(int col1, int col2) {
        Collections.swap(positionPicked, col1, col2);
    }

    @Override
    public void incorrectMove() {
        userInterface.incorrectMove();
    }

}
