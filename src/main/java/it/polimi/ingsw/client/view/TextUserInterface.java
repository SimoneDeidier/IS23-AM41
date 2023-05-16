package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientontroller.controller.ClientController;

public class TextUserInterface implements UserInterface{

    private final ClientController controller;

    public TextUserInterface(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        System.out.println("STARTED CLI");
    }

}

