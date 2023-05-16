package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;

public class TextUserInterface implements UserInterface{

    private ClientController clientController;

    @Override
    public void run() {
        System.out.println("STARTED CLI");
    }

    @Override
    public void setClientController(ClientController clientController) {

    }

    @Override
    public void getGameParameters() {

    }

}

