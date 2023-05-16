package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;
import it.polimi.ingsw.server.servercontroller.GameController;

public interface UserInterface {

    void run();

    ClientController getClientController();

    void setClientController(ClientController clientController);
}
