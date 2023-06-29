package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.clientcontroller.controller.ClientController;
import it.polimi.ingsw.messages.NewView;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import javafx.scene.Node;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Handles the TUI of the game
 */
public class TextUserInterface implements UserInterface{

    /**
     * This attribute holds the client controller
     */
    private static ClientController clientController;
    /**
     * This attribute keep track if it is the user turn or not
     */
    private boolean isYourTurn = false;
    /**
     * Scanner attribute to scan for input
     */
    private Scanner scanner;

    /**
     * This attribute define the width of the scene displayed on the terminal
     */
    private final int sceneWidth = 150;
    /**
     * This attribute define the height of the scene displayed on the terminal
     */
    private final int sceneHeight = 35;

    /**
     * This attribute holds the number of the personal target card assigned to the user
     */
    private int personalTargetCardNumber;
    /**
     * This attribute holds the nickname of the user
     */
    private String nickname;
    /**
     * This attribute keep track if the chat is open or not
     */
    private boolean chatOpen;
    /**
     * This attribute keep a record of all the messages received or sent into the chat
     */
    private final List<String> chatList = new ArrayList<>();

    /**
     * This attribute keeps track of the position of the items on the board selected by the user
     */
    private boolean[][] selectedBoardBitMask = new boolean[9][9];

    /**
     * This attribute define a list with the items selected by the user
     */
    private List<Item> selectedItems = new ArrayList<>(3);
    /**
     * This attribute keeps track of the coordinates of the selected items
     */
    private List<int[]> selectedItemsCoordinates = new ArrayList<>(3);

    /**
     * This attribute holds the last view provided by the controller
     */
    private NewView newView = new NewView();
    /**
     * This attribute keeps track of the items that are takeable on the board
     */
    private boolean[][] takeableItems;

    /**
     * This attribute keeps track if the TUI has been closed or not
     */
    private volatile boolean closeTUI = false;

    /**
     * String containing the current error to display
     */
    private String CommunicationText = "";
    /**
     * This attribute keeps track if the user is currently on the main game page
     */
    private boolean onMainGamePage = true;

    /**
     * Starting method to run the TUI
     */
    @Override
    public void run() {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Welcome to MyShelfie... CLI edition!");
        textLines.add("Resize the terminal window to have the border lines touching the terminal borders.");
        textLines.add("Press enter to start");

        standardTextPage(textLines);

        System.out.print("Your input: ");
        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            getNickname("Insert your nickname");
        }
        while (!closeTUI) {
            Thread.onSpinWait();
        }
    }

    /**
     * Creates a standard TUI page including only the text provided
     * @param textLines The list of lines to display on the page..
     */
    private void standardTextPage(List<String> textLines) {
        System.out.println(drawHorizontalLine(sceneWidth));

        drawEmptyLines(textLines.size());

        for (String str : textLines) {
            printContentLine(str);
        }

        drawEmptyLines(textLines.size());
    }

    /**
     * Ask the user for the nickname and send it to the sendNickname() method
     * @param text Text to display on the page to ask for the nickname
     */
    private void getNickname(String text) {

        /**
         * This attribute holds the nickname provided by the user input
         */
        String nickname;

        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add(text);

        standardTextPage(textLines);

        System.out.print("Your input: ");

        scanner = new Scanner(System.in);

        nickname = scanner.nextLine();

        if(nickname != null && !nickname.equals("") && nickname.length() < 20){
            sendNickname(nickname);
        } else {
            getNickname("The nickname you inserted is not valid. Insert your nickname:");
        }
    }

    /**
     * Display the main game page with the board and the user's shelf
     */
    private void mainGamePage() {

        /**
         * Number of columns in the board
         */
        int boardCol = 9;
        /**
         * Number of rows in the board
         */
        int boardRow = 9;
        /**
         * Number of columns in the shelf
         */
        int shelfCol = 5;

        /**
         * Width of a tile
         */
        int tileWidth = 8;

        /**
         * Space between the board and the shelf in the main page tui
         */
        int spaceBetweenBoardAndShelf = 10;

        /**
         * Flag used for input validation
         */
        boolean validInput = false;

        /**
         * Bitmask of the usable items on the board
         */
        boolean[][] boardBitMask = this.newView.getBoardBitMask();
        Item[][] boardItems = this.newView.getBoardItems();
        Item[][] shelfItems = this.newView.getNicknameToShelfMap().get(this.nickname);
        /**
         * Total points scored by the user so far
         */
        int totalPoints = this.newView.getNicknameToPointsMap().get(this.nickname);

        /**
         * String holding the topshelf title
         */
        String shelfTopString = "My Shelf!";

        /**
         * String holding the name of the chairholder
         */
        String chairHolder = "";

        /**
         * String holding the current turn situation
         */
        String currentTurn;

        onMainGamePage = true;

        System.out.println(drawHorizontalLine(sceneWidth));
        if(Objects.equals(this.newView.getPlayerList().get(0), nickname)) {
            chairHolder = "  --  You are the holder of the chair!";
        }
        if(isYourTurn()){
            currentTurn = " -- It's your turn";
        } else {
            currentTurn = " -- Current turn: " + this.newView.getActivePlayer();
        }
        printContentLine(this.nickname + " -- Total Points: " + totalPoints + chairHolder + currentTurn);
        System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
        printContentLine("\u001B[31m" + CommunicationText + "\u001B[0m");
        CommunicationText = "";


        for(int i=0; i<boardRow; i++){
            String repeat = " ".repeat(shelfCol * tileWidth + 1 + spaceBetweenBoardAndShelf);
            if(i==0){
                /**
                 * Interface line builder
                 */
                StringBuilder BoardColNumbers = new StringBuilder();
                for(int n=0; n<9; n++){
                    BoardColNumbers.append(" ".repeat(4));
                    BoardColNumbers.append(n);
                    BoardColNumbers.append(" ".repeat(3));
                }
                printContentLine(BoardColNumbers + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                for(int j=0; j<2; j++){
                    /**
                     * Interface line builder
                     */
                    StringBuilder shelfBoxLine = new StringBuilder();
                    if(j==0){
                        shelfBoxLine.append(i);
                    } else {
                        shelfBoxLine.append(" ");
                    }
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                if(this.takeableItems != null && this.takeableItems[i][w]){
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));
                                } else {
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));//MA PIù GRIGINO
                                }
                            } else {
                                shelfBoxLine.append(" ");
                            }
                        }
                        shelfBoxLine.append("|");
                    }
                    if(j==0 && isYourTurn()){
                        String selectedCardsText = "Your selected cards:";
                        printContentLine(shelfBoxLine + " ".repeat(spaceBetweenBoardAndShelf) + selectedCardsText + " ".repeat(shelfCol*tileWidth+1-selectedCardsText.length()));
                    } else {
                        printContentLine(shelfBoxLine + repeat);
                    }
                }
            } else if(i==1) {
                if(isYourTurn()){
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10+tileWidth) + drawHorizontalLine(3*tileWidth+1) + " ".repeat(tileWidth));
                } else {
                    printContentLine(drawHorizontalLine(boardCol * tileWidth + 1) + repeat);
                }
                for(int j=0; j<2; j++){
                    /**
                     * Interface line builder
                     */
                    StringBuilder shelfBoxLine = new StringBuilder();
                    if(j==0){
                        shelfBoxLine.append(i);
                    } else {
                        shelfBoxLine.append(" ");
                    }
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                if(this.takeableItems != null && this.takeableItems[i][w]){
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));
                                } else {
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));//MA PIù GRIGINO
                                }
                            } else {
                                shelfBoxLine.append(" ");
                            }
                        }
                        shelfBoxLine.append("|");
                    }
                    if(isYourTurn()) {
                        shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf+tileWidth));
                        shelfBoxLine.append("|");
                        for (int w = 0; w < 3; w++) {
                            for (int k = 0; k < tileWidth - 1; k++) {
                                if (getPositionPickedSize() > w) {
                                    shelfBoxLine.append(itemToColour(selectedItems.get(w)));
                                } else {
                                    shelfBoxLine.append(" ");
                                }
                            }
                            shelfBoxLine.append("|");
                        }
                        shelfBoxLine.append(" ".repeat(tileWidth));
                        printContentLine(shelfBoxLine.toString());
                    } else {
                        printContentLine(shelfBoxLine + repeat);
                    }
                }
            } else if(i==2) {
                if(isYourTurn()){
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10+tileWidth) + drawHorizontalLine(3*tileWidth+1) + " ".repeat(tileWidth));
                } else {
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + repeat);
                }
                for(int j=0; j<2; j++){
                    /**
                     * Interface line builder
                     */
                    StringBuilder shelfBoxLine = new StringBuilder();
                    if(j==0){
                        shelfBoxLine.append(i);
                    } else {
                        shelfBoxLine.append(" ");
                    }
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                if(this.takeableItems != null && this.takeableItems[i][w]){
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));
                                } else {
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));//MA PIù GRIGINO
                                }
                            } else {
                                shelfBoxLine.append(" ");
                            }
                        }
                        shelfBoxLine.append("|");
                    }
                    if(j==0) {
                        shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                        printContentLine(shelfBoxLine + drawHorizontalLine(shelfCol*tileWidth+1));
                    } else {
                        shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                        shelfBoxLine.append("|");
                        String repeat1 = " ".repeat((shelfCol * tileWidth - 1) / 2 - shelfTopString.length() / 2);
                        shelfBoxLine.append(repeat1).append(shelfTopString).append(repeat1);
                        shelfBoxLine.append("|");
                        printContentLine(shelfBoxLine.toString());
                    }

                }
            } else {
                if(i==3){
                    /**
                     * Interface line builder
                     */
                    StringBuilder ShelfColNumbers = new StringBuilder();
                    for(int n=0; n<5; n++){
                        ShelfColNumbers.append("-".repeat(4));
                        ShelfColNumbers.append(n);
                        ShelfColNumbers.append("-".repeat(3));
                    }
                    ShelfColNumbers.append("-");
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10) + ShelfColNumbers);
                } else {
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10) + drawHorizontalLine(shelfCol*tileWidth+1));
                }
                for(int j=0; j<2; j++){
                    /**
                     * Interface line builder
                     */
                    StringBuilder shelfBoxLine = new StringBuilder();
                    if(j==0){
                        shelfBoxLine.append(i);
                    } else {
                        shelfBoxLine.append(" ");
                    }
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                if(this.takeableItems != null && this.takeableItems[i][w]){
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));
                                } else {
                                    shelfBoxLine.append(itemToColour(boardItems[i][w]));//MA PIù GRIGINO
                                }
                            } else {
                                shelfBoxLine.append(" ");
                            }
                        }
                        shelfBoxLine.append("|");
                    }
                    shelfBoxLine.append(" ".repeat(spaceBetweenBoardAndShelf));
                    shelfBoxLine.append("|");
                    for(int w=0; w<shelfCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(shelfItems[i-3][w] != null){
                                shelfBoxLine.append(itemToColour(shelfItems[i-3][w]));
                            } else {
                                shelfBoxLine.append(" ");
                            }
                        }
                        shelfBoxLine.append("|");
                    }
                    printContentLine(shelfBoxLine.toString());
                }
            }
        }
        printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10) + drawHorizontalLine(shelfCol*tileWidth+1));

        if(isYourTurn()){
            printContentLine("Commands: Your tokens (/tokens), Personal Goals (/personal), Common Goals (/common), Other Players (/other), Chat (/chat)");
            printContentLine("Turn commands: Select tile (/select), remove tile (/remove)");
            printContentLine("Turn commands: Swap Columns (/swap), Insert into column (/column)");
        } else {
            printContentLine("Commands: Your tokens (/tokens), Personal Goals (/personal), Common Goals (/common), Other Players (/other), Chat (/chat)");
            System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
            System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
        }

        System.out.print("Your input: ");

        /**
         * Scanner for user input
         */
        scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        switch (command) {
            case "/tokens" -> {
                onMainGamePage = false;
                tokensPage();
            }
            case "/personal" -> {
                onMainGamePage = false;
                personalGoalsPage();
            }
            case "/common" -> {
                onMainGamePage = false;
                commonGoalsPage();
            }
            case "/other" -> {
                onMainGamePage = false;
                otherPlayersPage();
            }
            case "/chat" -> {
                onMainGamePage = false;
                chat();
            }
            case "/swap" -> {
                if(isYourTurn()) {
                    System.out.print("Insert the index (0, 1 or 2) of the first column you want to swap: ");
                    int first = scanner.nextInt();
                    System.out.print("Insert the index (0, 1 or 2) of the second column you want to swap: ");
                    int second = scanner.nextInt();
                    if(first < getPositionPickedSize() && second < getPositionPickedSize() && first != second){
                        Collections.swap(selectedItems, first, second);
                        Collections.swap(selectedItemsCoordinates, first, second);
                        swapColsTUI(first, second);
                        mainGamePage();
                    } else {
                        tileNotAvailable();
                    }
                } else {
                    forbiddenInput();
                }
            }
            case "/column" -> {
                if(isYourTurn()) {
                    System.out.print("Insert the index of the column you want to insert the tiles into: ");
                    int column = scanner.nextInt();
                    if(column >= 0 && column < 5){
                        if(columnHasEnoughSpace(column)){
                            sendMove(column);
                        } else {
                            forbiddenInput();
                        }
                    } else {
                        forbiddenInput();
                    }
                } else {
                    forbiddenInput();
                }
            }
            case "/select" -> {
                if(isYourTurn()) {
                    if(getPositionPickedSize()<3) {
                        int[] el = new int[2];
                        while (!validInput) {
                            try {
                                System.out.print("Insert the row coordinate (from 0 to 8) of the tile you want to select: ");
                                el[0] = scanner.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.print("Invalid input! Insert the row coordinate (from 0 to 8) of the tile you want to select: ");
                                scanner.nextLine(); // Clear the invalid input from the scanner
                            }
                        }
                        validInput = false;
                        while (!validInput) {
                            try {
                                System.out.print("Insert the column coordinate (from 0 to 8) of the tile you want to select: ");
                                el[1] = scanner.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.print("Invalid input! Insert the column coordinate (from 0 to 8) of the tile you want to select: ");
                                scanner.nextLine(); // Clear the invalid input from the scanner
                            }
                        }
                        if(boardBitMask[el[0]][el[1]] && !selectedBoardBitMask[el[0]][el[1]]){
                            selectedBoardBitMask[el[0]][el[1]] = true;
                            selectedItems.add(boardItems[el[0]][el[1]]);
                            selectedItemsCoordinates.add(el);
                            insertInPositionPicked(el);
                            mainGamePage();
                        } else {
                            tileNotAvailable();
                        }
                    }
                } else {
                    forbiddenInput();
                }
            }
            case "/remove" -> {
                if(isYourTurn()) {
                    if(getPositionPickedSize()>0) {
                        System.out.print("Insert the index (0, 1 or 2) of the item you want to remove: ");
                        int itemToRemove = scanner.nextInt();
                        if(selectedItems.size()>itemToRemove){
                            int[] el = selectedItemsCoordinates.get(itemToRemove);
                            selectedItemsCoordinates.remove(itemToRemove);
                            selectedItems.remove(itemToRemove);
                            selectedBoardBitMask[el[0]][el[1]] = false;
                            removeInPositionPicked(itemToRemove);
                            mainGamePage();
                        } else {
                            tileNotAvailable();
                        }
                    } else {
                        forbiddenInput();
                    }
                } else {
                    forbiddenInput();
                }
            }
            default -> {
                forbiddenInput();
            }
        }

    }

    /**
     * Warns the user, displaying it on screen, that the input inserted is either wrong or forbidden
     */
    private void forbiddenInput() {

        CommunicationText = "You inserted an input that is either wrong or forbidden for you at the moment. Try again.";
        mainGamePage();
    }

    /**
     * Warns the user that the tile selected is not actually available, or it doesn't exist
     */
    private void tileNotAvailable() {
        CommunicationText = "You selected a tile that doesn't exist...";
        mainGamePage();
    }

    /**
     * Convert an item to the corresponding text and color to display on screen
     * @param item Item to convert into a string
     */
    private String itemToColour(Item item) {
        if(item != null) {
            return switch (item.getColor()) {
                case LIGHT_BLUE -> "\u001B[46mL\u001B[0m";
                case BLUE -> "\u001B[44mB\u001B[0m";
                case GREEN -> "\u001B[42mG\u001B[0m";
                case YELLOW -> "\u001B[43mY\u001B[0m";
                case WHITE -> "\u001B[47mW\u001B[0m";
                case PINK -> "\u001B[45mP\u001B[0m";
            };
        }
        return " ";
    }

    /**
     * Display the tokens earned by the user throughout the game
     */
    private void tokensPage() {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();
        /**
         * This attribute keeps track if there's at least one token assigned to the user
         */
        boolean tokenFound=false;

        // Add strings to the list
        textLines.add("The following are the tokens you scored for this game:");
        textLines.add(" ");
        textLines.add(" ");
        for (ScoringToken scoringToken : this.newView.getPlayersToTokens().get(this.nickname)){
            textLines.add("Token value: " + scoringToken.getValue());
            tokenFound = true;
        }
        if(!tokenFound){
            textLines.add("No tokens were found...");
        }
        textLines.add(" ");
        textLines.add("Press enter to go back");


        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    /**
     * Display the personal goal card assigned to the user
     */
    private void personalGoalsPage() {
        /**
         * Number of columns of the shelf
         */
        int shelfCol = 5;
        /**
         * Number of rows of the shelf
         */
        int shelfRow = 6;

        /**
         * Width of a shelf tile
         */
        int tileWidth = 8;

        Item[][] shelfItems = new Item[6][5];

        Gson gson = new Gson();
        String jsonString = null;
        try {
            jsonString = IOUtils.toString(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("json/PersonalTargetCards.json")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Could not load a resource from a JSON file, please restart the game!");
        }

        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        JsonObject TargetCard = jsonArray.get(this.personalTargetCardNumber).getAsJsonObject();

        Object[][] colourArray = {{"pink", ItemColor.PINK}, {"light_blue", ItemColor.LIGHT_BLUE}, {"yellow", ItemColor.YELLOW}, {"blue", ItemColor.BLUE}, {"white", ItemColor.WHITE}, {"green", ItemColor.GREEN}};

        for (Object[] colour : colourArray) {
            shelfItems[TargetCard.getAsJsonObject((String) ((Object[]) colour)[0]).get("row").getAsInt()][TargetCard.getAsJsonObject((String) ((Object[]) colour)[0]).get("column").getAsInt()] = new Item((ItemColor) ((Object[]) colour)[1]);
        }

        System.out.println(drawHorizontalLine(sceneWidth));

        for(int i=0; i<7; i++){
            System.out.println("|"+" ".repeat(sceneWidth-2)+"|");
        }

        printContentLine(drawHorizontalLine(shelfCol*tileWidth+1));
        String repeat = " ".repeat((shelfCol * tileWidth - 1) / 2 - this.nickname.length() / 2);
        String shelfBoxLine_top = "|" +
                repeat + this.nickname + repeat +
                "|";
        printContentLine(shelfBoxLine_top);
        for(int i=0; i<shelfRow; i++){
            printContentLine(drawHorizontalLine(shelfCol*tileWidth+1));
            for(int j=0; j<2; j++){
                /**
                 * Interface line builder
                 */
                StringBuilder shelfBoxLine = new StringBuilder();
                shelfBoxLine.append("|");
                for(int w=0; w<shelfCol; w++) {
                    for (int k = 0; k < tileWidth-1; k++) {
                        if(shelfItems[i][w] != null){
                            shelfBoxLine.append(itemToColour(shelfItems[i][w]));
                        } else {
                            shelfBoxLine.append(" ");
                        }
                    }
                    shelfBoxLine.append("|");
                }
                printContentLine(shelfBoxLine.toString());
            }
        }
        printContentLine(drawHorizontalLine(shelfCol*tileWidth+1));

        for(int i=0; i<3; i++){
            printContentLine("");
        }

        printContentLine("Press enter to go back");

        for(int i=0; i<3; i++){
            printContentLine("");
        }

        System.out.println(drawHorizontalLine(sceneWidth));
        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    /**
     * Display the common card goals of the current game
     */
    private void commonGoalsPage() {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("The following is (are) the Common Target Card(s) for this game:");
        textLines.add(" ");
        this.newView.getCommonsToTokens().forEach((key, value) -> {
            switch (key) {
                case "CommonSixGroupsOfTwo" -> {
                    textLines.add("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).");
                    textLines.add("The tiles of one group can be different from those of another group.");
                }
                case "CommonDiagonal" -> textLines.add("Five tiles of the same type forming a diagonal.");
                case "CommonEightSame" ->
                        textLines.add("Eight tiles of the same type. There’s no restriction about the position of these tiles.");
                case "CommonFourCorners" ->
                        textLines.add("Four tiles of the same type in the four corners of the bookshelf.");
                case "CommonFourGroupsOfFour" -> {
                    textLines.add("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape).");
                    textLines.add("The tiles of one group can be different from those of another group.");
                }
                case "CommonFourRows" -> {
                    textLines.add("Four lines each formed by 5 tiles of maximum three different types.");
                    textLines.add("One line can show the same or a different combination of another line.");
                }
                case "CommonStairway" -> {
                    textLines.add("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right,");
                    textLines.add("each next column must be made of exactly one more tile. Tiles can be of any type.");
                }
                case "CommonThreeColumns" -> {
                    textLines.add("Three columns each formed by 6 tiles of maximum three different types.");
                    textLines.add("One column can show the same or a different combination of another column.");
                }
                case "CommonTwoColumns" -> textLines.add("Two columns each formed by 6 different types of tiles.");
                case "CommonTwoRows" -> {
                    textLines.add("Two lines each formed by 5 different types of tiles.");
                    textLines.add("One line can show the same or a different combination of the other line.");
                }
                case "CommonTwoSquares" -> {
                    textLines.add("Two groups each containing 4 tiles of the same type in a 2x2 square.");
                    textLines.add("The tiles of one square can be different from those of the other square.");
                }
                case "CommonX" -> textLines.add("Five tiles of the same type forming an X.");
                default -> textLines.add("NO COMMON TARGET CARD SET");
            }
            textLines.add(" ");
        });
        textLines.add(" ");
        textLines.add("Press enter to go back");


        standardTextPage(textLines);

        System.out.print(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    /**
     * Display the shelfs of the other players
     */
    private void otherPlayersPage() {

        /**
         * Number of columns of the shelf
         */
        int shelfCol = 5;
        /**
         * Number of rows of the shelf
         */
        int shelfRow = 6;

        /**
         * Width of a shelft tile
         */
        int tileWidth = 8;
        /**
         * Number of other players
         */
        int otherPlayersNumber = this.newView.getNicknameToShelfMap().size()-1;

        System.out.println(drawHorizontalLine(sceneWidth));

        for(int i=0; i<7; i++){
            printContentLine("");
        }

        printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(otherPlayersNumber));
        /**
         * Interface line builder
         */
        StringBuilder shelfBoxLine_top = new StringBuilder();
        for (Map.Entry<String, Item[][]> entry : this.newView.getNicknameToShelfMap().entrySet()) {
            String nickname = entry.getKey();
            if(!Objects.equals(nickname, this.nickname)) {
                shelfBoxLine_top.append("|");
                String repeat = " ".repeat((shelfCol * tileWidth - 1) / 2 - nickname.length() / 2);
                shelfBoxLine_top.append(repeat).append(nickname).append(repeat);
                shelfBoxLine_top.append("|");
                shelfBoxLine_top.append(" ".repeat(5));
            }
        }
        printContentLine(shelfBoxLine_top.toString());

            for(int i=0; i<shelfRow; i++){
                printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(otherPlayersNumber));
                for(int j=0; j<2; j++){
                    /**
                     * Interface line builder
                     */
                    StringBuilder shelfBoxLine = new StringBuilder();
                    for (Map.Entry<String, Item[][]> entry : this.newView.getNicknameToShelfMap().entrySet()) {
                        String nickname = entry.getKey();
                        if(!Objects.equals(nickname, this.nickname)) {
                            Item[][] shelfItems = entry.getValue();
                            shelfBoxLine.append("|");
                            for (int w = 0; w < shelfCol; w++) {
                                for (int k = 0; k < tileWidth - 1; k++) {
                                    if (shelfItems[i][w] != null) {
                                        shelfBoxLine.append(itemToColour(shelfItems[i][w]));
                                    } else {
                                        shelfBoxLine.append(" ");
                                    }
                                }
                                shelfBoxLine.append("|");
                            }
                            shelfBoxLine.append(" ".repeat(5));
                        }
                    }
                    printContentLine(shelfBoxLine.toString());
                }
            }
        printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(otherPlayersNumber));

        for(int i=0; i<3; i++){
            printContentLine("");
        }

        printContentLine("Press enter to go back");

        for(int i=0; i<3; i++){
            printContentLine("");
        }

        System.out.println(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    /**
     * Display the chat of the game
     */
    private void chat() {
        this.chatOpen = true;
        /**
         * Number of messages sent on the chat
         */
        int chatSize;
        chatSize = this.chatList.size();
        printContentLine(drawHorizontalLine(this.sceneWidth));
        if(this.sceneHeight>chatSize){
            for(int i=0; i<this.sceneHeight-chatSize; i++){
                System.out.println();
            }
        }
        for(int j=0; j<chatSize; j++){
            System.out.println(chatList.get(j));
        }
        if(chatSize == 0){
            System.out.println("The chat seems to be empty, be the first to write something! Or type /back to go back to the main page.");
        } else {
            System.out.println();
            System.out.println("Type your message or type /back to go back to the main page.");
        }
        /**
         * Scanner for user input
         */
        scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        while(!message.equals("/back")) {
            if(message != ""){
                sendMessage(message);
            }
            scanner = new Scanner(System.in);
            message = scanner.nextLine();
        }
            this.chatOpen = false;
            mainGamePage();
    }

    /**
     * Print a line of content centred with the lateral bars used in standard pages
     *  @param text string of content to centre
     */
    private void printContentLine(String text) {
        /**
         * Interface line builder
         */
        StringBuilder lineContent = new StringBuilder();
        /**
         * Flag counter to distinguish number of blocks
         */
        int count=0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '[') {
                count++;
            }
        }
        /**
         * Actual lenght of the text without characters to give the text a back color
         */
        int text_lenght = text.length() - (count/2 * 9);
        if(text_lenght % 2 != 0){
            text = text + " ";
        }
        lineContent.append(" ".repeat(Math.max(0, (sceneWidth - text_lenght - 2) / 2)));
        String contentLine = "|" + lineContent + text + lineContent + "|";
        System.out.println(contentLine);
    }

    /**
     * Print as many empty lines as needed to vertically centre the content
     * @param contentLines number of content lines to centre
     */
    private void drawEmptyLines(int contentLines) {
        /**
         * Interface line builder
         */
        StringBuilder line = new StringBuilder();
        line.append(" ".repeat((sceneWidth - 2)));

        /**
         * Number of necessary empty lines
         */
        int emptyLines = (sceneHeight - contentLines) / 2;

        for (int i = 0; i < emptyLines; i++) {
            System.out.println("|" + line + "|");
        }
    }

    /**
     * Prints an horizontal line
     * @param width width of the line to print
     */
    private String drawHorizontalLine(int width) {
        return "-".repeat(Math.max(0, width));
    }

    /**
     * Set the clientController
     * @param clientController provide the clientController to be set
     */
    @Override
    public void setClientController(ClientController clientController) {
        new Thread(() -> {
            TextUserInterface.clientController = clientController;
        }).start();
    }

    /**
     * Ask the first user for the game parameters: number of players and number of common target cards
     */
    @Override
    public void getGameParameters() {
        new Thread(() -> {
            /**
             * Number of players
             */
            int numPlayers = getNumberOfPlayers();
            /**
             * Number of common target cards
             */
            int numCommons = getNumberOfCommon();
            sendParameters(numPlayers, numCommons);
        }).start();
    }

    /**
     * Ask the first user for the number of players
     */
    private int getNumberOfPlayers() {

        /**
         * Attribute to keep track if the input is valid or not
         */
        boolean validInput = false;

        /**
         * Number of players received by the user
         */
        int number=0;

        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of players!");
        textLines.add("It can be either 2, 3 or 4");

        standardTextPage(textLines);

        System.out.print("Your input: ");

        /**
         * Scanner for user input
         */
        scanner = new Scanner(System.in);

        while (!validInput) {
            try {
                number = scanner.nextInt();
                if(number < 2 || number > 4){
                    throw new Exception();
                }
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter an integer from 2 to 4.");
                System.out.print("Your input: ");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }

        return number;
    }

    /**
     * Ask the first user for the number of common target cards
     */
    private int getNumberOfCommon() {

        /**
         * Attribute to keep track if the input is valid or not
         */
        boolean validInput = false;

        /**
         * Number of common target cards received by the user
         */
        int number=0;

        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of common target cards!");
        textLines.add("It can be either 1 or 2");

        standardTextPage(textLines);

        System.out.print("Your input: ");

        /**
         * Scanner for user input
         */
        scanner = new Scanner(System.in);

        while (!validInput) {
            try {
                number = scanner.nextInt();
                if(number < 1 || number > 2){
                    throw new Exception();
                }
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter an integer from 1 to 2.");
                System.out.print("Your input: ");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }

        return number;
    }

    /**
     * Send the nickname to the client controller
     * @param nickname the nickname to send to the client controller
     */
    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
        clientController.startClearThread();
    }

    /**
     * Warn the user that the name is already in use
     */
    @Override
    public void invalidNickname() {
        new Thread(() -> {
            getNickname("The username is already used, please insert another nickname!");
        }).start();
    }

    /**
     * Send the parameters to the client controller
     * @param numPlayers the number of players
     * @param numCommons the number of common target cards
     */
    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        clientController.sendParameters(numPlayers, numCommons);
    }

    /**
     * Tells the user that the nickname has been accepted and that he's in the lobby
     */
    @Override
    public void nicknameAccepted(int nPlayers, Map<String, Boolean> lobby) {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Nickname accepted. You are in a lobby! Please wait for the game start!");

            standardTextPage(textLines);
            System.out.print(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Tells the user that a lobby was created and that the game will start soon.
     */
    @Override
    public void lobbyCreated(int nPlayers, Map<String, Boolean> lobby) {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You are in a lobby! Please wait for the game start!");

        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));
    }

    /**
     * Tells the user that someone else is trying to create a lobby and to retry in a few seconds
     */
    @Override
    public void waitForLobby() {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Someone is trying to create a lobby, please retry in a few seconds!");

            standardTextPage(textLines);
            System.out.print(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Save data useful for the game to start
     * @param personalTargetCardNumber the number of the personal target card picked for the user
     * @param nickname the nickname of the user
     * @param commonTargetGoals list of strings with the common target goals
     */
    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {
        new Thread(() -> {
            this.personalTargetCardNumber = personalTargetCardNumber;
            this.nickname = nickname;
        }).start();
    }

    /**
     * Send a message from the chat to the client controller
     * @param message message to send to the client controller
     */
    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    /**
     * Receive a message from the chat and save it to display in the chat
     * @param message message received
     * @param sender nickname of the person that sent the message
     * @param localDateTime date and time of when the message was sent
     */
    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        /**
         * String that holds the formatted chat line
         */
        String completeChatLine = sender + " at " + localDateTime + ": " + message;
        if(this.chatOpen){
            System.out.println(completeChatLine);
        }
        this.chatList.add(completeChatLine);
    }

    /**
     * Receive all the data necessary to update the text user interface
     * @param newView NewView object containing all the updated data for the interface
     */
    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        new Thread(() -> {
            if(newView.isGameOver()){
                exit();
            }
            this.isYourTurn = Objects.equals(newView.getActivePlayer(), this.nickname);
            this.newView = newView;
            if(onMainGamePage){
                mainGamePage();
            }
        }).start();
    }

    /**
     * Calls the exit method from the client controller to end the game and notify the user that the game is over
     */
    @Override
    public void exit() {
        clientController.exit();
        closeTUI = true;

        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("GAME OVER!");
        textLines.add("");
        textLines.add("The Game is over, you can close the client!");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));
    }

    /**
     * Tells the user that he rejoined a game he left before
     */
    @Override
    public void rejoinedMatch() {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You have been rejoined to a lobby! Please wait for the game!");

        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));
    }

    /**
     * Warns the user that he tried to send a message to a non existant user
     */
    @Override
    public void invalidPlayer() {
        new Thread(() -> {
            /**
             * String that holds the error to display as a chat line
             */
            String completeChatLine = "ERROR: You tried to send a personal message to an user that doesn't exist";
            if(this.chatOpen){
                System.out.println(completeChatLine);
            }
        }).start();
    }

    /**
     * returns the isYourTurn parameter that determines if it is the user turn or not
     * @return boolean referring to the user turn
     */
    @Override
    public boolean isYourTurn() {
        return isYourTurn;
    }

    /**
     * Send the column where to put the selected items for the move to the client controller
     * @param col column to send to the client controller
     */
    @Override
    public void sendMove(int col) {
        clientController.sendMove(col);
    }

    /**
     * Send to the controller the coordinates of the element chosen by the user
     * @param el coordinates of the element picked
     */
    @Override
    public void insertInPositionPicked(int[] el) {
        clientController.insertInPositionPicked(el);
    }

    /**
     * Checks if the user has already picked the maximum amount of items for the move
     */
    @Override
    public int getPositionPickedSize() {
        return clientController.getPositionPickedSize();
    }

    @Override
    public void swapColsGUI(List<Node> list) {
        // NO CLI
    }

    @Override
    public int getSwapColIndex(Node n) {
            // NO CLI
            return 0;
    }

    /**
     * Swap the order of two selected items
     * @param col1 first column to swap
     * @param col2 second column to swap
     */
    @Override
    public void swapColsTUI(int col1, int col2) {
        clientController.swapCols(col1, col2);
    }

    /**
     * Warns the user that his move is incorrect and that the board has been reset to the previous state
     */
    @Override
    public void incorrectMove() {
        this.selectedBoardBitMask = new boolean[9][9];
        this.selectedItems = new ArrayList<>(3);
        this.selectedItemsCoordinates = new ArrayList<>(3);

        CommunicationText = "Your move is incorrect, the board has been reset to the previous state.";
        mainGamePage();
    }

    /**
     * Warns the user that he tried to send a personal message to a non-existent user
     */
    @Override
    public void wrongReceiver() {
        new Thread(() -> {
            /**
             * String that holds the error to display as a chat line
             */
            String completeChatLine = "ERROR: You inserted a wrong nickname to send a personal message!";
            if(this.chatOpen){
                System.out.println(completeChatLine);
            }
        }).start();
    }

    /**
     * Warns the user that he provided parameters that are not acceptable for the game
     */
    @Override
    public void wrongParameters() {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Either the number of players or the number of common target cards is not acceptable.");
            textLines.add("Press enter to choose new parameters.");

            standardTextPage(textLines);

            System.out.print("Your input: ");

            scanner = new Scanner(System.in);

            if(scanner.hasNextLine()){
                getGameParameters();
            }
        }).start();
    }

    /**
     * Send the column selected to the client controller to check if there's enough space to insert the items the user selected
     * @param col column to check
     * @return boolean telling if the column has enough space or not
     */
    @Override
    public boolean columnHasEnoughSpace(int col) {
        return clientController.columnHasEnoughSpace(col);
    }

    /**
     * Send to the controller the column of the selected items that the user wants to remove
     * @param col column where to remove the item
     */
    @Override
    public void removeInPositionPicked(int col) {
        clientController.removeInPositionPicked(col);
    }

    /**
     * Tells the user that he's been restored from a previous saved game and that he's now waiting in the lobby
     */
    @Override
    public void playerRestored() {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("The player has been restored from a previous saved game.");
            textLines.add("You are now in a lobby waiting to play.");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Warns the user that the server is not responding
     */
    @Override
    public void serverNotResponding() {
        new Thread(() -> {

            clientController.exit();
            closeTUI = true;

            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("SERVER NOT RESPONDING");
            textLines.add("Server is momentarily unreachable. Please, wait a few seconds and restart the client!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Tells the user that the lobby has been restored.
     */
    @Override
    public void lobbyRestored() {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Lobby has been restored! Please wait for the game start!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Warns the user that the lobby is full, and to close the client and try again
     */
    @Override
    public void fullLobby() {
        /**
         * This attribute holds an arraylist of the text lines to display
         */
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("The lobby is full! Close the client and retry later!");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));
    }

    /**
     * Warns the user that the lobby cannot be restored because a player that wasn't in the lobby tried to join
     */
    @Override
    public void cantRestoreLobby() throws IOException {
        new Thread(() -> {
            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("CAN'T RESTORE LOBBY");
            textLines.add("A player that wasn't in the last lobby has tried to connect! Please, restart the client, a new lobby will be created!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Congratulate the user for the win because all the other players left for too much time
     */
    @Override
    public void alonePlayerWins() {
        new Thread(() -> {

            clientController.exit();
            closeTUI = true;

            /**
             * This attribute holds an arraylist of the text lines to display
             */
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("YOU HAVE WON!!!");
            textLines.add("All the other players were disconnected for an excessive amount of time! Restart the client to create a new lobby!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    /**
     * Tells the user that a user disconnected from the game
     * @param nickname nickname of the user that disconnected
     */
    @Override
    public void playerDisconnected(String nickname) {
        new Thread(() -> {
            CommunicationText = nickname + " has disconnected from the game";
            mainGamePage();
        }).start();
    }

    /**
     * Tells the user that a user reconnected to the game
     * @param nickname nickname of the user that reconnected
     */
    @Override
    public void playerReconnected(String nickname) {
        new Thread(() -> {
            CommunicationText = nickname + " has reconnected to the game";
            mainGamePage();
        }).start();
    }

    /**
     * Register which parameters are takeable on the board
     * @param takeableItems a bitmask of the takeable items on the board
     * @param yourTurn a boolean telling if it is the user turn or not
     * @param waitForOtherPlayers a boolean about other players turns
     */
    @Override
    public void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers) {
        new Thread(() -> {
            if(yourTurn && !waitForOtherPlayers) {
                this.takeableItems = takeableItems;
            }
        }).start();
    }

    /**
     * Calls the client controller method to exit from the game without disconnecting from the server
     */
    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        clientController.exitWithoutWaitingDisconnectFromServer();
        closeTUI = true;
    }

    @Override
    public void userConnected(String nickname) {

    }

    @Override
    public void disconnectedFromLobby(String nickname) {

    }

    @Override
    public void userRejoined(String nickname) {

    }
}

