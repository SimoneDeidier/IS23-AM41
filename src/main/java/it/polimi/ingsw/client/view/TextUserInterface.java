package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements UserInterface{

    private static ClientController clientController;
    Scanner scanner;

    @Override
    public void run() {
        System.out.println("STARTED CLI");
        scanner = new Scanner(System.in);
    }

    @Override
    public void setClientController(ClientController clientController) {
        TextUserInterface.clientController = clientController;
    }

    @Override
    public void getGameParameters() {
        //Unsure about which parameters need to be asked to the user
    }

    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
    }

    @Override
    public void invalidNickname() {
        System.out.println("The username is already used, please insert another nickname!");
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        clientController.sendParameters(numPlayers, numCommons);
    }

    @Override
    public void nicknameAccepted() {
        System.out.println("You are in a lobby! Please wait for the game start!");
    }

    @Override
    public void lobbyCreated() {
        System.out.println("You are in a lobby! Please wait for the game start!");
    }

    @Override
    public void waitForLobby() {
        System.out.println("Someone is trying to create a lobby, please retry in a few seconds!");
    }

    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {

    }

    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {

    }

    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void rejoinMatch() {

    }

    @Override
    public void close() {

    }

    @Override
    public void rejoinedMatch() {

    }

    @Override
    public void invalidPlayer() {

    }

}

