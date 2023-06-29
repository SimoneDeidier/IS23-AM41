package it.polimi.ingsw.client.clientcontroller;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.client.clientcontroller.controller.ClientControllerTCP;
import it.polimi.ingsw.interfaces.TCPMessageControllerInterface;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class TCPMessageController implements TCPMessageControllerInterface {

    private final SerializeDeserialize serializeDeserialize;
    private final ClientController controller;
    private static final int CLEAR_DELAY = 1000;
    private int clearUnanswered = 0;
    private boolean closeClearThread = false;
    private boolean wasIJustReconnected = false;

    public TCPMessageController(SerializeDeserialize serializeDeserialize) {
        this.serializeDeserialize = serializeDeserialize;
        controller = new ClientControllerTCP(this);
    }

    @Override
    public void readTCPMessage(TCPMessage message) throws IOException, URISyntaxException {
        String header = message.getHeader();
        switch (header) {
            case "Nickname Accepted" -> {
                Body body = message.getBody();
                controller.nicknameAccepted(body.getNumberOfPlayers(), body.getLobby());
            }
            case "Wait for Lobby" -> {
                controller.waitForLobby();
                stopClearThread();
            }
            case "Lobby Restored" -> {
                controller.lobbyRestored();
            }
            case "Player Restored" -> {
                controller.playerRestored();
            }
            case "Invalid Nickname" -> {
                controller.invalidNickname();
            }
            case "Goodbye" -> {
                switch (message.getBody().getGoodbyeType()) {
                    case 0 -> {
                        controller.cantRestoreLobby();
                    }
                    case 1 -> {
                        controller.alonePlayerWins();
                    }
                    case 2 -> {}
                    default -> System.err.println("INCORRECT GOODBYE TCP MESSAGE!");
                }
                closeClient();
            }
            case "Get Parameters" -> {
                controller.getParameters();
            }
            case "Your Target" -> {
                controller.setPersonalTargetCardNumber(message.getBody().getPersonalCardNumber());
                controller.setCommonGoalList(message.getBody().getCommonTargetCardsName());
                if(!wasIJustReconnected) {
                    controller.loadGameScreen();
                }
            }
            case "Update View" -> {
                if(wasIJustReconnected) {
                    controller.loadGameScreen();
                    wasIJustReconnected = false;
                }
                controller.updateView(message.getBody().getNewView());
            }
            case "Lobby Created" -> {
                Body body = message.getBody();
                controller.lobbyCreated(body.getNumberOfPlayers(), body.getLobby());
            }
            case "Wrong Parameters" -> {
                controller.wrongParameters();
            }
            case "Incorrect Move" -> {
                controller.incorrectMove();
            }
            case "Wrong Receiver" -> {
                controller.wrongReceiver();
            }
            case "New Msg" -> {
                controller.receiveMessage(message.getBody().getText(), message.getBody().getSenderNickname(), message.getBody().getLocalDateTime());
            }
            case "Rejoined" -> {
                this.wasIJustReconnected = true;
                controller.rejoinedMatch();
            }
            case "Invalid Player" -> {
                controller.invalidPlayer();
            }
            case "Check" -> {
                clearUnanswered = 0;
            }
            case "Full Lobby" -> {
                controller.fullLobby();
                closeClient();
            }
            case "Player Disconnected" -> {
                controller.playerDisconnected(message.getBody().getPlayerNickname());
            }
            case "Player Reconnected" -> {
                controller.playerReconnected(message.getBody().getPlayerNickname());
            }
            case "User Connected" -> {
                controller.userConnected(message.getBody().getPlayerNickname());
            }
            case "Disconnected From Lobby" -> {
                controller.disconnectedFromLobby(message.getBody().getPlayerNickname());
            }
            case "Rejoined In Lobby" -> {
                controller.nicknameAccepted(message.getBody().getNumberOfPlayers(), message.getBody().getLobby());
            }
            case "User Rejoined" -> {
                controller.userRejoined(message.getBody().getPlayerNickname());
            }
        }
    }

    @Override
    public void printTCPMessage(String header, Body body) {
        TCPMessage tcpMessage = new TCPMessage(header, body);
        serializeDeserialize.serialize(tcpMessage);
    }

    public void closeConnection() {
        serializeDeserialize.closeConnection();
    }

    public void startUserInterface(String uiType) {
        controller.startUserInterface(uiType);
    }

    public String getPlayerNickname() {
        return controller.getPlayerNickname();
    }

    public void startClearThread() {
        closeClearThread = false;
        new Thread(() -> {
            while(clearUnanswered < 5 && !closeClearThread) {
                printTCPMessage("Clear", null);
                clearUnanswered++;
                try {
                    Thread.sleep(CLEAR_DELAY);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread was unexpectedly terminated, this could cause problems during the game!");
                }
            }
            if(!closeClearThread) {
                controller.serverNotResponding();
            }
        }).start();
    }

    public void stopClearThread() {
        this.closeClearThread = true;
    }

    public void closeClient() {
        this.stopClearThread();
        this.closeConnection();
    }

}
