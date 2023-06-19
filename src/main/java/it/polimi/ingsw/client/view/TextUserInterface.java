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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TextUserInterface implements UserInterface{

    private static ClientController clientController;
    private boolean isYourTurn = false;
    private Scanner scanner;

    private final int sceneWidth = 150;
    private final int sceneHeight = 35;

    private int personalTargetCardNumber;
    private String nickname;
    private boolean chatOpen;
    private final List<String> chatList = new ArrayList<>();

    private boolean[][] selectedBoardBitMask = new boolean[9][9];

    private List<Item> selectedItems = new ArrayList<>(3);
    private List<int[]> selectedItemsCoordinates = new ArrayList<>(3);

    private NewView newView = new NewView();
    private boolean[][] takeableItems;
    private volatile boolean closeTUI = false;

    @Override
    public void run() {
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

    private void standardTextPage(List<String> textLines) {
        System.out.println(drawHorizontalLine(sceneWidth));

        drawEmptyLines(textLines.size());

        for (String str : textLines) {
            printContentLine(str);
        }

        drawEmptyLines(textLines.size());
    }

    private void getNickname(String text) {

        String nickname;

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

    private void mainGamePage() {

        int boardCol = 9;
        int boardRow = 9;
        int shelfCol = 5;

        int tileWidth = 8;

        int spaceBetweenBoardAndShelf = 10;

        boolean validInput = false;

        boolean[][] boardBitMask = this.newView.getBoardBitMask();
        Item[][] boardItems = this.newView.getBoardItems();
        Item[][] shelfItems = this.newView.getNicknameToShelfMap().get(this.nickname);
        int totalPoints = this.newView.getNicknameToPointsMap().get(this.nickname);

        String shelfTopString = "My Shelf!";

        String chairHolder = "";

        System.out.println(drawHorizontalLine(sceneWidth));
        if(Objects.equals(this.newView.getPlayerList().get(0), nickname)) {
            chairHolder = "  --  You are the holder of the chair!";
        }
        printContentLine(this.nickname + " -- Total Points: " + totalPoints + chairHolder);
        System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
        if(isYourTurn()){
            printContentLine("It's your turn");
        } else {
            printContentLine("Current turn: " + this.newView.getActivePlayer());
        }

        for(int i=0; i<boardRow; i++){
            String repeat = " ".repeat(shelfCol * tileWidth + 1 + spaceBetweenBoardAndShelf);
            if(i==0){
                StringBuilder BoardColNumbers = new StringBuilder();
                for(int n=0; n<9; n++){
                    BoardColNumbers.append(" ".repeat(4));
                    BoardColNumbers.append(n);
                    BoardColNumbers.append(" ".repeat(3));
                }
                printContentLine(BoardColNumbers + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                for(int j=0; j<2; j++){
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

        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        switch (command) {
            case "/tokens" -> tokensPage();
            case "/personal" -> personalGoalsPage();
            case "/common" -> commonGoalsPage();
            case "/other" -> otherPlayersPage();
            case "/chat" -> chat();
            case "/swap" -> {
                if(isYourTurn()) {
                    System.out.print("Insert the index (0, 1 or 2) of the first column you want to swap: ");
                    int first = in.nextInt();
                    System.out.print("Insert the index (0, 1 or 2) of the second column you want to swap: ");
                    int second = in.nextInt();
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
                    int column = in.nextInt();
                    if(columnHasEnoughSpace(column)){
                        System.out.println("**************************************** Mossa inviata");
                        sendMove(column);
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
                                el[0] = in.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.print("Invalid input! Insert the row coordinate (from 0 to 8) of the tile you want to select: ");
                                in.nextLine(); // Clear the invalid input from the scanner
                            }
                        }
                        validInput = false;
                        while (!validInput) {
                            try {
                                System.out.print("Insert the column coordinate (from 0 to 8) of the tile you want to select: ");
                                el[1] = in.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.print("Invalid input! Insert the column coordinate (from 0 to 8) of the tile you want to select: ");
                                in.nextLine(); // Clear the invalid input from the scanner
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
                        int itemToRemove = in.nextInt();
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

    private void forbiddenInput() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You inserted an input that is either wrong or forbidden for you at the moment. Try again.");
        textLines.add(" ");
        textLines.add(" ");
        textLines.add("Press enter to go back");


        standardTextPage(textLines);

        System.out.print(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    private void tileNotAvailable() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You selected a tile that doesn't exist...");
        textLines.add(" ");
        textLines.add(" ");
        textLines.add("Press enter to go back");


        standardTextPage(textLines);

        System.out.print(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

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

    private void tokensPage() {
        List<String> textLines = new ArrayList<>();
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

    private void personalGoalsPage() {
        int shelfCol = 5;
        int shelfRow = 6;

        int tileWidth = 8;

        String nickname = this.nickname;
        Item[][] shelfItems = new Item[6][5];

        Gson gson = new Gson();
        File jsonFile = null;
        try {
            jsonFile = new File(ClassLoader.getSystemResource("json/PersonalTargetCards.json").toURI());
        } catch (URISyntaxException e) {
            System.err.println("Could not load a resource from a JSON file, please restart the game!");
        }
        String jsonString = null;
        try {
            jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);
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
        String repeat = " ".repeat((shelfCol * tileWidth - 1) / 2 - nickname.length() / 2);
        String shelfBoxLine_top = "|" +
                repeat + nickname + repeat +
                "|";
        printContentLine(shelfBoxLine_top);
        for(int i=0; i<shelfRow; i++){
            printContentLine(drawHorizontalLine(shelfCol*tileWidth+1));
            for(int j=0; j<2; j++){
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

    private void commonGoalsPage() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("The following is (are) the Common Target Card(s) for this game:");
        textLines.add(" ");
        this.newView.getCommonsToTokens().forEach((key, value) -> {
            switch (key) {
                case "CommonSixGroupsOfTwo" ->
                        textLines.add("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape). The tiles of one group can be different from those of another group.");
                case "CommonDiagonal" -> textLines.add("Five tiles of the same type forming a diagonal.");
                case "CommonEightSame" ->
                        textLines.add("Eight tiles of the same type. There’s no restriction about the position of these tiles.");
                case "CommonFourCorners" ->
                        textLines.add("Four tiles of the same type in the four corners of the bookshelf.");
                case "CommonFourGroupsOfFour" ->
                        textLines.add("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape). The tiles of one group can be different from those of another group.");
                case "CommonFourRows" ->
                        textLines.add("Four lines each formed by 5 tiles of maximum three different types. One line can show the same or a different combination of another line.");
                case "CommonStairway" ->
                        textLines.add("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right, each next column must be made of exactly one more tile. Tiles can be of any type.");
                case "CommonThreeColumns" ->
                        textLines.add("Three columns each formed by 6 tiles of maximum three different types. One column can show the same or a different combination of another column.");
                case "CommonTwoColumns" -> textLines.add("Two columns each formed by 6 different types of tiles.");
                case "CommonTwoRows" ->
                        textLines.add("Two lines each formed by 5 different types of tiles. One line can show the same or a different combination of the other line.");
                case "CommonTwoSquares" ->
                        textLines.add("Two groups each containing 4 tiles of the same type in a 2x2 square. The tiles of one square can be different from those of the other square.");
                case "CommonX" -> textLines.add("Five tiles of the same type forming an X.");
                default -> textLines.add("NO COMMON TARGET CARD SET");
            }
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

    private void otherPlayersPage() {

        int shelfCol = 5;
        int shelfRow = 6;

        int tileWidth = 8;
        int mapSize = this.newView.getNicknameToShelfMap().size()-1;

        System.out.println(drawHorizontalLine(sceneWidth));

        for(int i=0; i<7; i++){
            printContentLine("");
        }

        printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(mapSize));
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
                printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(mapSize));
                for(int j=0; j<2; j++){
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
        printContentLine((drawHorizontalLine(shelfCol*tileWidth+1)+" ".repeat(5)).repeat(mapSize));

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

    private void chat() {
        this.chatOpen = true;
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
            System.out.println("The chat seems to be empty, be the first to write something!");
        }

        Scanner in = new Scanner(System.in);
        String message = in.nextLine();
        while(!message.equals("/back")) {
            sendMessage(message);
        }
            this.chatOpen = false;
            mainGamePage();
    }

    private void printContentLine(String text) {
        StringBuilder lineContent = new StringBuilder();
        int count=0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '[') {
                count++;
            }
        }
        int text_lenght = text.length() - (count/2 * 9);
        if(text_lenght % 2 != 0){
            text = text + " ";
        }
        lineContent.append(" ".repeat(Math.max(0, (sceneWidth - text_lenght - 2) / 2)));
        String contentLine = "|" + lineContent + text + lineContent + "|";
        System.out.println(contentLine + text_lenght + " " + text.length());
    }

    private void drawEmptyLines(int contentLines) {
        //Center content
        StringBuilder line = new StringBuilder();
        line.append(" ".repeat((sceneWidth - 2)));

        // Calculate the number of empty lines above and below the content
        int emptyLines = (sceneHeight - contentLines) / 2;

        // Print empty lines above/below the content
        for (int i = 0; i < emptyLines; i++) {
            System.out.println("|" + line + "|");
        }
    }

    private String drawHorizontalLine(int width) {
        return "-".repeat(Math.max(0, width));
    }

    @Override
    public void setClientController(ClientController clientController) {
        new Thread(() -> {
            TextUserInterface.clientController = clientController;
        }).start();
    }

    @Override
    public void getGameParameters() {
        new Thread(() -> {
            int numPlayers = getNumberOfPlayers();
            int numCommons = getNumberOfCommon();
            sendParameters(numPlayers, numCommons);
        }).start();
    }

    private int getNumberOfPlayers() {

        boolean validInput = false;

        int number=0;

        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of players!");
        textLines.add("It can be either 2, 3 or 4");

        standardTextPage(textLines);

        System.out.print("Your input: ");

        Scanner in = new Scanner(System.in);

        while (!validInput) {
            try {
                number = in.nextInt();
                if(number < 2 || number > 4){
                    throw new Exception();
                }
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter an integer from 2 to 4.");
                System.out.print("Your input: ");
                in.nextLine(); // Clear the invalid input from the scanner
            }
        }

        return number;
    }

    private int getNumberOfCommon() {

        boolean validInput = false;

        int number=0;

        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Insert number of common target cards!");
        textLines.add("It can be either 1 or 2");

        standardTextPage(textLines);

        System.out.print("Your input: ");

        Scanner in = new Scanner(System.in);

        while (!validInput) {
            try {
                number = in.nextInt();
                if(number < 1 || number > 2){
                    throw new Exception();
                }
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter an integer from 1 to 2.");
                System.out.print("Your input: ");
                in.nextLine(); // Clear the invalid input from the scanner
            }
        }

        return number;
    }

    @Override
    public void sendNickname(String nickname) {
        clientController.sendNickname(nickname);
        clientController.startClearThread();
    }

    @Override
    public void invalidNickname() {
        new Thread(() -> {
            getNickname("The username is already used, please insert another nickname!");
        }).start();
    }

    @Override
    public void sendParameters(int numPlayers, int numCommons) {
        clientController.sendParameters(numPlayers, numCommons);
    }

    @Override
    public void nicknameAccepted() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("You are in a lobby! Please wait for the game start!");

            standardTextPage(textLines);
            System.out.print(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void lobbyCreated() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You are in a lobby! Please wait for the game start!");

        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));
    }

    @Override
    public void waitForLobby() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Someone is trying to create a lobby, please retry in a few seconds!");

            standardTextPage(textLines);
            System.out.print(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void loadGameScreen(int personalTargetCardNumber, String nickname, List<String> commonTargetGoals) {
        new Thread(() -> {
            this.personalTargetCardNumber = personalTargetCardNumber;
            this.nickname = nickname;
        }).start();
    }

    @Override
    public void sendMessage(String message) {
        clientController.sendMessage(message);
    }

    @Override
    public void receiveMessage(String message, String sender, String localDateTime) {
        String completeChatLine = sender + " at " + localDateTime + ": " + message;
        if(this.chatOpen){
            System.out.println(completeChatLine);
        }
        this.chatList.add(completeChatLine);
    }

    @Override
    public void updateView(NewView newView) throws FileNotFoundException, URISyntaxException {
        if(newView.isGameOver()){
            gameOver();
        }
        this.isYourTurn = Objects.equals(newView.getActivePlayer(), this.nickname);
        this.newView = newView;

        new Thread(this::mainGamePage).start();
    }

    private void gameOver() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("GAME OVER!");
        textLines.add("");
        textLines.add("Press enter to exit!");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            exit();
        }
    }

    @Override
    public void exit() {
        clientController.exit();
        closeTUI = true;
    }

    @Override
    public void rejoinedMatch() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("You have been rejoined to a lobby! Please wait for the game!");

        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));
    }

    @Override
    public void invalidPlayer() {
        new Thread(() -> {
            String completeChatLine = "ERROR: You tried to send a personal message to an user that doesn't exist";
            if(this.chatOpen){
                System.out.println(completeChatLine);
            }
        }).start();
    }

    @Override
    public boolean isYourTurn() {
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
        // passi al controller le coordinate di un elemento scelto
        clientController.insertInPositionPicked(el);
    }

    @Override
    public int getPositionPickedSize() {
        // check se ha già scelto o meno
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

    @Override
    public void swapColsTUI(int col1, int col2) {
        clientController.swapCols(col1, col2);
    }

    @Override
    public void incorrectMove() {
        this.selectedBoardBitMask = new boolean[9][9];
        this.selectedItems = new ArrayList<>(3);
        this.selectedItemsCoordinates = new ArrayList<>(3);

        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Your move is incorrect, the board has been reset to the previous state.");
        textLines.add("");
        textLines.add("Press enter to go back!");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));

        scanner = new Scanner(System.in);

        if(scanner.hasNextLine()){
            mainGamePage();
        }
    }

    @Override
    public void wrongReceiver() {
        new Thread(() -> {
            String completeChatLine = "ERROR: You inserted a wrong nickname to send a personal message!";
            if(this.chatOpen){
                System.out.println(completeChatLine);
            }
        }).start();
    }

    @Override
    public void wrongParameters() {
        new Thread(() -> {
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

    @Override
    public boolean columnHasEnoughSpace(int col) {
        return clientController.columnHasEnoughSpace(col);
    }

    @Override
    public void removeInPositionPicked(int col) {
        clientController.removeInPositionPicked(col);
    }

    @Override
    public void playerRestored() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("The player has been restored from a previous saved game.");
            textLines.add("You are now in a lobby waiting to play.");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void serverNotResponding() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("SERVER NOT RESPONDING");
            textLines.add("Server is momentarily unreachable. Please, wait a few seconds and restart the client!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void lobbyRestored() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("Lobby has been restored! Please wait for the game start!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void fullLobby() {
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("The lobby is full! Close the client and retry later!");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));
    }

    @Override
    public void cantRestoreLobby() throws IOException {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("CAN'T RESTORE LOBBY");
            textLines.add("A player that wasn't in the last lobby has tried to connect! Please, restart the client, a new lobby will be created!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void alonePlayerWins() {
        new Thread(() -> {
            List<String> textLines = new ArrayList<>();

            // Add strings to the list
            textLines.add("YOU HAVE WON!!!");
            textLines.add("All the other  players were disconnected for an excessive longer period! Restart the client to create a new lobby!");

            standardTextPage(textLines);
            System.out.println(drawHorizontalLine(sceneWidth));
        }).start();
    }

    @Override
    public void playerDisconnected(String nickname) {
        new Thread(() -> {
        //Non so come fare qui, non è molto fattibile in CLI per la questione dei thread e delle funzione che aspettano un input
        }).start();
    }

    @Override
    public void playerReconnected(String nickname) {
        new Thread(() -> {
            //same as above
        }).start();
    }

    @Override
    public void setTakeableItems(boolean[][] takeableItems, boolean yourTurn, boolean waitForOtherPlayers) {
        new Thread(() -> {
            if(yourTurn && !waitForOtherPlayers) {
                this.takeableItems = takeableItems;
            }
        }).start();
    }

    @Override
    public void exitWithoutWaitingDisconnectFromServer() {
        clientController.exitWithoutWaitingDisconnectFromServer();
        closeTUI = true;
    }

}

