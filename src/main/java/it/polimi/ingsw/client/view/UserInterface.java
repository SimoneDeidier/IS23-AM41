package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.IOException;

public interface UserInterface {

    void run();
    void setClientController(ClientController clientController);
    void getGameParameters();
}
