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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements UserInterface{

    private static ClientController clientController;
    private boolean isYourTurn = false; //todo deve essere modificato quando arriva new view
    private Scanner scanner;

    int sceneWidth = 150;
    int sceneHeight = 30;

    int personalTargetCardNumber;
    String nickname;
    List<String> commonTargetGoals;

    @Override
    public void run() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Welcome to MyShelfie... CLI edition!");
        textLines.add("Resize the terminal window to have the border lines touching the terminal borders.");
        textLines.add("Press any key to start");

        standardTextPage(textLines);

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            getNickname("Insert your nickname");
        }
    }

    private void standardTextPage(List<String> textLines) {
        System.out.println(drawHorizontalLine(sceneWidth));

        drawEmptyLines(textLines.size());

        for (String str : textLines) {
            printContentLine(str);
        }

        drawEmptyLines(textLines.size());

        System.out.print(drawHorizontalLine(sceneWidth));
    }

    private void getNickname(String text) {

        String nickname;

        System.out.println(drawHorizontalLine(sceneWidth));

        drawEmptyLines(1);

        printContentLine(text);

        drawEmptyLines(1);

        System.out.print(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        nickname = scanner.nextLine();

        sendNickname(nickname);
    }

    private void mainGamePage(NewView newView) {

        int boardCol = 9;
        int boardRow = 9;
        int shelfRow = 6;
        int shelfCol = 5;

        int tileWidth = 8;

        int spaceBetweenBoardAndShelf = 10;

        String shelfTopString = "My Shelf!";

        printContentLine(this.nickname + " - Total Points: " );
        printContentLine("Current turn:" + newView.getActivePlayer());

        for(int i=0; i<boardRow; i++){
            if(i<2){
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardRow; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            shelfBoxLine.append(" ");
                        }
                        shelfBoxLine.append("|");
                    }

                    printContentLine(shelfBoxLine.toString() + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                }
            } else if(i==2) {
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardRow; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            shelfBoxLine.append(" ");
                        }
                        shelfBoxLine.append("|");
                    }
                    if(j==0) {
                        shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                        printContentLine(shelfBoxLine + drawHorizontalLine(shelfCol*tileWidth+1));
                    } else {
                        shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                        shelfBoxLine.append("|");
                        shelfBoxLine.append(" ".repeat((shelfCol*tileWidth-1)/2 - shelfTopString.length()/2) + shelfTopString + " ".repeat((shelfCol*tileWidth-1)/2 - shelfTopString.length()/2));
                        shelfBoxLine.append("|");
                        printContentLine(shelfBoxLine.toString());
                    }

                }
            } else {
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10) + drawHorizontalLine(shelfCol*tileWidth+1));
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            shelfBoxLine.append(" ");
                        }
                        shelfBoxLine.append("|");
                    }
                    shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                    shelfBoxLine.append("|");
                    for(int w=0; w<shelfCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            shelfBoxLine.append(" ");
                        }
                        shelfBoxLine.append("|");
                    }
                    printContentLine(shelfBoxLine.toString());
                }
            }
        }
        printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10) + drawHorizontalLine(shelfCol*tileWidth+1));

        printContentLine("Commands: Your tokens (/tokens), Personal Goals (/personal), Common Goals (/common), Other Players (/other), Chat (/chat)");
        System.out.print(drawHorizontalLine(sceneWidth));

        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        switch (command) {
            case "/tokens" -> {
                tokensPage();
            }
            case "/personal" -> {
                personalGoalsPage();
            }
            case "/common" -> {
                commonGoalsPage();
            }
            case "/other" -> {
                otherPlayersPage();
            }
            case "/chat" -> {
                chat();
            }
        }

    }

    private void tokensPage() {
    }

    private void personalGoalsPage() {
    }

    private void commonGoalsPage() {
    }

    private void otherPlayersPage() {
    }

    private void chat() {
    }



    private void personalTargetPage() {
        for(int i=0; i<6; i++){
            printContentLine(drawHorizontalLine(5*8+1));
            for(int j=0; j<2; j++){
                StringBuilder shelfBoxLine = new StringBuilder();
                shelfBoxLine.append("|");
                for(int w=0; w<5; w++) {
                    for (int k = 0; k < 7; k++) {
                        shelfBoxLine.append(" ");
                    }
                    shelfBoxLine.append("|");
                }
                printContentLine(shelfBoxLine.toString());
            }
        }
        printContentLine(drawHorizontalLine(5*8+1));
    }

    private void printContentLine(String text) {
        StringBuilder lineContent = new StringBuilder();
        for (int i = 0; i < (sceneWidth - text.length() - 2)/2; i++) {
            lineContent.append(" ");
        }
        String contentLine = "|" + lineContent + text + lineContent + "|";
        System.out.println(contentLine);
    }

    private void drawEmptyLines(int contentLines) {
        //Center content
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < (sceneWidth-2); i++) {
            line.append(" ");
        }

        // Calculate the number of empty lines above and below the content
        int emptyLines = (sceneHeight - contentLines) / 2;

        // Print empty lines above/below the content
        for (int i = 0; i < emptyLines; i++) {
            System.out.println("|" + line.toString() + "|");
        }
    }

    private String drawHorizontalLine(int width) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < width; i++) {
            line.append("-");
        }
        return line.toString();
    }

    @Override
    public void setClientController(ClientController clientController) {
        TextUserInterface.clientController = clientController;
    }

    @Override
    public void getGameParameters() {
        int numPlayers = getNumberOfPlayers();
        int numCommons = getNumberOfCommon();
        sendParameters(numPlayers, numCommons);
    }

    private int getNumberOfPlayers() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of players!");
        textLines.add("It can be either 2, 3 or 4");

        standardTextPage(textLines);

        Scanner in = new Scanner(System.in);
        int command = in.nextInt();

        return command;
    }

    private int getNumberOfCommon() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of common target cards!");
        textLines.add("It can be either 1 or 2");

        standardTextPage(textLines);

        Scanner in = new Scanner(System.in);
        int command = in.nextInt();

        return command;
    }

    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
    }

    @Override
    public void invalidNickname() {
        getNickname("The username is already used, please insert another nickname!");
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        clientController.sendParameters(numPlayers, numCommons);
    }

    @Override
    public void nicknameAccepted() {

        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You are in a lobby! Please wait for the game start!");

        standardTextPage(textLines);
    }

    @Override
    public void lobbyCreated() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You are in a lobby! Please wait for the game start!");

        standardTextPage(textLines);
    }

    @Override
    public void waitForLobby() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Someone is trying to create a lobby, please retry in a few seconds!");

        standardTextPage(textLines);
    }

    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {
        this.personalTargetCardNumber = personalTargetCardNumber;
        this.nickname = nickname;
        this.commonTargetGoals = commonTargetGoals;
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
        //se newView.gameover è true, fai vedere pagina di gameover


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
    public void exit() {
        // chiude tutto ed esce
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
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Either the number of players or the number of common target cards is not acceptable.");
        textLines.add("Press enter to choose new parameters.");

        standardTextPage(textLines);

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            getGameParameters();
        }
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

    @Override
    public void serverNotResponding() {

    }

    @Override
    public void lobbyRestored() {

    }

}

