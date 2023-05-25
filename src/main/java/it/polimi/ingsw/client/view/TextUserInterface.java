package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.items.Item;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements UserInterface{

    private static ClientController clientController;
    private boolean isYourTurn = false; // todo deve essere modificato quando arriva new view
    private Scanner scanner;

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
        // cambi grafica dicendo di reinserire in nome
        System.out.println("The username is already used, please insert another nickname!");
        // scanner sull'input
        //sendNickname();
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
        // ti salvi tutti i valori passati
    }

    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        // if Utente è sulla chat ->
            // stampi a video il nuovo messaggio
        // lo salvi nella lista
    }

    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        // stampi su cli tutte le cose che arrivano
        // scanner che aspetta un input
        // giga switch con il comando che ha scelto
            // se inserisce comandi sulla mossa ==> CHECK CHE è il suo turno
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        switch (command) {
            case "/swap" -> {
                System.out.println("Inserisci le colonne da swappare: ");
                String colonne_da_swappare = scanner.nextLine();
                // check
                // trasformi la stringa in due int
                int uno = 1;
                int due = 3;
                swapCols(uno, due);
            }
            case "/colonna" -> {
                // stessa cosa chiedi la colonna
                int colonna = 3;
                sendMove(colonna);
            }
        }
    }

    @Override
    public void disconnect() {
        // da sistemare nel controller
    }

    @Override
    public void rejoinMatch() {
        // da sistemare nel controller
    }

    @Override
    public void exit() {
        // da sistemare nel controller
    }

    @Override
    public void rejoinedMatch() {
        // da sistemare nel controller
    }

    @Override
    public void invalidPlayer() {
        // errore se un utente manda un messaggio personale ad un giocatore che non esiste
    }

    @Override
    public boolean isYourTurn() {
        // stampa a video una scritta per dire che è il tuo turno
        return isYourTurn;
    }

    @Override
    public void sendMove(int col) {
        // passa al controller la colonna
        // il controller in automatico manda la mossa
        clientController.sendMove(col);
    }

    @Override
    public void insertInPositionPicked(int[] el) {
        // passi al controller le coordinate di un elemento scleto
        clientController.insertInPositionPicked(el);
    }

    @Override
    public int getPositionPickedSize() {
        // check se ha già scelto o meno
        return clientController.getPositionPickedSize();
    }

    @Override
    public void swapCols(List<Node> list) {
        // NO CLI
    }

    @Override
    public int getSwapColIndex(Node n) {
        // NO CLI
        return 0;
    }

    @Override
    public void swapCols(int col1, int col2) {
        clientController.swapCols(col1, col2);
    }

    @Override
    public void incorrectMove() {
        // avvisi di mossa scorretta
        // stampa la board non modificata
    }

    @Override
    public void wrongReceiver() {
        // show the user that has inserted a wrong nickname to send a personal message
    }

    @Override
    public void wrongParameters() {
        // show the user has inserted wrong parameters of the lobby (es.: players > 4 o commons > 2)
    }

    @Override
    public boolean columnHasEnoughSpace(int col) {
        return clientController.columnHasEnoughSpace(col);
    }

    @Override
    public void removeInPositionPicked(int col) {
        clientController.columnHasEnoughSpace(col);
    }

    @Override
    public void playerRestored() {
        // scrive a schermo che un giocatore è stato restorato da un salvataggio
    }

}

