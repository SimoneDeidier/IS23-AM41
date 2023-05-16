package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;

public class TextUserInterface implements UserInterface{

    private ClientController clientController;

    @Override
    public void run() {
        System.out.println("STARTED CLI");
    }

    @Override
    public ClientController getClientController() {
        return null;
    }

    @Override
    public void setClientController(ClientController clientController) {

    }

}

