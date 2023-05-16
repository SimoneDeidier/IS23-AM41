package it.polimi.ingsw.client.clientontroller.controller;

import it.polimi.ingsw.client.clientontroller.TCPMessageController;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.TextUserInterface;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.messages.Body;

public class ClientControllerTCP implements ClientController {

    private final TCPMessageController tcpMessageController;
    private UserInterface userInterface = null;
    private Thread userInterfaceThread = null;

    public ClientControllerTCP(TCPMessageController tcpMessageController) {
        this.tcpMessageController = tcpMessageController;
    }

    public void startUserInterface(String uiType) {
        switch (uiType) {
            case "gui" -> userInterface = new GraphicUserInterface();
            case "tui" ->  userInterface = new TextUserInterface();
        }
        userInterface.setClientController(this);
        System.out.println(userInterface.getClientController());
        userInterfaceThread = new Thread(() -> userInterface.run());
        userInterfaceThread.start();
        try {
            userInterfaceThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNickname(String nickname) {
        Body body = new Body();
        body.setPlayerNickname(nickname);
        tcpMessageController.printTCPMessage("Presentation", body);
    }

}
