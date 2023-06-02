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
    private List<String> commonTargetGoals;
    private boolean chatOpen;
    private List<String> chatList = new ArrayList<>();

    private boolean[][] selectedBoardBitMask = new boolean[9][9];

    private List<Item> selectedItems = new ArrayList<>(3);
    private List<int[]> selectedItemsCoordinates = new ArrayList<>(3);

    private NewView newView = new NewView();
    private boolean firstUpdateView;

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

        sendNickname(nickname);
    }

    private void mainGamePage() {

        int boardCol = 9;
        int boardRow = 9;
        int shelfCol = 5;

        int tileWidth = 8;

        int spaceBetweenBoardAndShelf = 10;

        boolean[][] boardBitMask = this.newView.getBoardBitMask();
        Item[][] boardItems = this.newView.getBoardItems();
        Item[][] shelfItems = this.newView.getNicknameToShelfMap().get(this.nickname);
        int totalPoints = this.newView.getNicknameToPointsMap().get(this.nickname);

        String shelfTopString = "My Shelf!";

        System.out.println(drawHorizontalLine(sceneWidth));
        printContentLine(this.nickname + " -- Total Points: " + totalPoints);
        System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
        if(Objects.equals(this.newView.getPlayerList().get(0), nickname)) {
            printContentLine("You are the holder of the chair!" );
        } else {
            System.out.println("|" + " ".repeat(sceneWidth - 2) + "|");
        }
        if(isYourTurn()){
            printContentLine("It's your turn");
        } else {
            printContentLine("Current turn: " + this.newView.getActivePlayer());
        }

        for(int i=0; i<boardRow; i++){
            if(i==0){
                printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                shelfBoxLine.append(itemToColour(boardItems[i][w]));
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
                        printContentLine(shelfBoxLine + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                    }
                }
            } else if(i==1) {
                if(isYourTurn()){
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10+tileWidth) + drawHorizontalLine(3*tileWidth+1) + " ".repeat(tileWidth));
                } else {
                    printContentLine(drawHorizontalLine(boardCol * tileWidth + 1) + " ".repeat(shelfCol * tileWidth + 1 + spaceBetweenBoardAndShelf));
                }
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                shelfBoxLine.append(itemToColour(boardItems[i][w]));
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
                        printContentLine(shelfBoxLine + " ".repeat(shelfCol * tileWidth + 1 + spaceBetweenBoardAndShelf));
                    }
                }
            } else if(i==2) {
                if(isYourTurn()){
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(10+tileWidth) + drawHorizontalLine(3*tileWidth+1) + " ".repeat(tileWidth));
                } else {
                    printContentLine(drawHorizontalLine(boardCol*tileWidth+1) + " ".repeat(shelfCol*tileWidth+1+spaceBetweenBoardAndShelf));
                }
                for(int j=0; j<2; j++){
                    StringBuilder shelfBoxLine = new StringBuilder();
                    shelfBoxLine.append("|");
                    for(int w=0; w<boardCol; w++) {
                        for (int k = 0; k < tileWidth-1; k++) {
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                shelfBoxLine.append(itemToColour(boardItems[i][w]));
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
                            if(boardBitMask[i][w] && !selectedBoardBitMask[i][w]){
                                shelfBoxLine.append(itemToColour(boardItems[i][w]));
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
                        swapCols(first, second);
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
                        int el[] = new int[2];
                        System.out.print("Insert the row coordinate (from 0 to 8) of the tile you want to select: ");
                        el[0] = in.nextInt();
                        System.out.print("Insert the column coordinate (from 0 to 8) of the tile you want to select: ");
                        el[1] = in.nextInt();
                        if(boardBitMask[el[0]][el[1]] == true && selectedBoardBitMask[el[0]][el[1]] == false){
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

    private char itemToColour(Item item) {
        return switch (item.getColor()) {
            case LIGHT_BLUE -> 'L';
            case BLUE -> 'B';
            case GREEN -> 'G';
            case YELLOW -> 'Y';
            case WHITE -> 'W';
            case PINK -> 'P';
        };
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
            throw new RuntimeException(e);
        }
        String jsonString = null;
        try {
            jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        StringBuilder shelfBoxLine_top = new StringBuilder();
        shelfBoxLine_top.append("|");
        shelfBoxLine_top.append(" ".repeat((shelfCol*tileWidth-1)/2 - nickname.length()/2) + nickname + " ".repeat((shelfCol*tileWidth-1)/2 - nickname.length()/2));
        shelfBoxLine_top.append("|");
        printContentLine(shelfBoxLine_top.toString());
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
                case "CommonSixGroupsOfTwo":
                    textLines.add("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape). The tiles of one group can be different from those of another group.");
                    break;
                case "CommonDiagonal":
                    textLines.add("Five tiles of the same type forming a diagonal.");
                    break;
                case "CommonEightSame":
                    textLines.add("Eight tiles of the same type. There’s no restriction about the position of these tiles.");
                    break;
                case "CommonFourCorners":
                    textLines.add("Four tiles of the same type in the four corners of the bookshelf.");
                    break;
                case "CommonFourGroupsOfFour":
                    textLines.add("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape). The tiles of one group can be different from those of another group.");
                    break;
                case "CommonFourRows":
                    textLines.add("Four lines each formed by 5 tiles of maximum three different types. One line can show the same or a different combination of another line.");
                    break;
                case "CommonStairway":
                    textLines.add("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right, each next column must be made of exactly one more tile. Tiles can be of any type.");
                    break;
                case "CommonThreeColumns":
                    textLines.add("Three columns each formed by 6 tiles of maximum three different types. One column can show the same or a different combination of another column.");
                    break;
                case "CommonTwoColumns":
                    textLines.add("Two columns each formed by 6 different types of tiles.");
                    break;
                case "CommonTwoRows":
                    textLines.add("Two lines each formed by 5 different types of tiles. One line can show the same or a different combination of the other line.");
                    break;
                case "CommonTwoSquares":
                    textLines.add("Two groups each containing 4 tiles of the same type in a 2x2 square. The tiles of one square can be different from those of the other square.");
                    break;
                case "CommonX":
                    textLines.add("Five tiles of the same type forming an X.");
                    break;
                default:
                    textLines.add("NO COMMON TARGET CARD SET");
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
            if(nickname != this.nickname) {
                shelfBoxLine_top.append("|");
                shelfBoxLine_top.append(" ".repeat((shelfCol * tileWidth - 1) / 2 - nickname.length() / 2) + nickname + " ".repeat((shelfCol * tileWidth - 1) / 2 - nickname.length() / 2));
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
                        if(nickname != this.nickname) {
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
            System.out.println("You: " + message);
            message = in.nextLine();
            chatList.add("You: " + message);
        }
            this.chatOpen = false;
            mainGamePage();
    }

    private void printContentLine(String text) {
        StringBuilder lineContent = new StringBuilder();
        if(text.length()%2!=0){
            text = text + " ";
        }
        lineContent.append(" ".repeat(Math.max(0, (sceneWidth - text.length() - 2) / 2)));
        String contentLine = "|" + lineContent + text + lineContent + "|";
        System.out.println(contentLine);
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
        StringBuilder line = new StringBuilder();
        line.append("-".repeat(Math.max(0, width)));
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
                if(number < 1 || number > 4){
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
        System.out.print(drawHorizontalLine(sceneWidth));
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
        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("Someone is trying to create a lobby, please retry in a few seconds!");

        standardTextPage(textLines);
        System.out.print(drawHorizontalLine(sceneWidth));
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
        this.firstUpdateView = true;

        mainGamePage();
    }

    private void gameOver() {
        //Devo solo fare vedere pagina di gameover? E poi? Devo segnalare come cominciare una nuova partita? O cos'altro?
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
        String completeChatLine = "ERROR: You tried to send a personal message to an user that doesn't exist";
        if(this.chatOpen){
            System.out.println(completeChatLine);
        }
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
        String completeChatLine = "ERROR: You inserted a wrong nickname to send a personal message!";
        if(this.chatOpen){
            System.out.println(completeChatLine);
        }
    }

    @Override
    public void wrongParameters() {
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

        List<String> textLines = new ArrayList<>();

        // Add strings to the list
        textLines.add("The player has been restored from a previous saved game.");
        textLines.add("You are now in a lobby waiting to play.");

        standardTextPage(textLines);
        System.out.println(drawHorizontalLine(sceneWidth));
    }

    @Override
    public void serverNotResponding() {

    }

    @Override
    public void lobbyRestored() {

    }

    @Override
    public void fullLobby() {

    }

    @Override
    public void cantRestoreLobby() throws IOException {

    }

    @Override
    public void alonePlayerWins() {

    }

    @Override
    public void playerDisconnected(String nickname) {

    }

}

