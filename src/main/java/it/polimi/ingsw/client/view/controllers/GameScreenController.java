package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.server.model.items.Item;
import it.polimi.ingsw.server.model.items.ItemColor;
import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * GameScreenController controls the game screen in the client's GUI.
 */
public class GameScreenController {

    private final static double OFFSET = 13.0;
    private final static double MAX_CHAT_MSG = 194.0;
    private final static int BOARD_DIM = 9;
    private final static double ITEM_DIM = 51.0;
    private final static double ITEM_OFFSET_LEFT = 5.0;
    private final static double PICKED_IT_DIM = 61.0;
    private final static int SHELF_ROWS = 6;
    private final static int SHELF_COL = 5;
    private final static double SHELF_ITEM_DIM = 41.0;
    private final static double SHELF_ITEM_OFFSET = 10.1;
    private final static double TRANSITION_DURATION_EFF = 1000.0;
    private final static long NOTIFY_DURATION = 3000;
    private final static double OPACITY_LOW = 0.0;
    private final static double OPACITY_HIGH = 0.9;
    private final static double EFFECT_DIM = 43.0;
    private final static Color EFFECT_COL = Color.rgb(84, 0, 255);

    private GraphicUserInterface gui;
    private Image personalGoalImage = null;
    private List<String> commonGoalNames = new ArrayList<>(2);
    private boolean onlyOneCommon;
    private Map<String, Item[][]> nicknameToShelfMap;
    private Map<String, Integer> nicknameToPointsMap;
    private String playerNickname;
    private Item[][] boardMatrx;
    private List<Node> swapCols = new ArrayList<>(2);
    private Map<String, List<ScoringToken>> commonsToTokens;
    private List<ScoringToken> playerTokens;
    private EndGameToken endGameToken = null;
    private String firstPlayer;
    private List<String> disconnectedPlayersToShow = new ArrayList<>();
    private boolean notifyIsOn = false;
    private boolean[][] takeableItems = null;

    @FXML
    private Text playerText;
    @FXML
    private TextArea chatMessageTextArea;
    @FXML
    private VBox chatVBox;
    @FXML
    private GridPane boardGridPane;
    @FXML
    private GridPane pickedItemsGridPane;
    @FXML
    private GridPane shelfGridPane;
    @FXML
    private AnchorPane turnAnchorPane;
    @FXML
    private ImageView chairImageView;
    @FXML
    private ImageView endGameTokenImageView;
    @FXML
    private AnchorPane notificationAnchorPane;
    @FXML
    private Label notificationLabel;
    @FXML
    private Label turnLabel;

    /**
     * Initialize the game screen
     */
    public void initialize() {
        chatVBox.setStyle("-fx-background-color: #442211;");
    }

    /**
     * Save the gui value
     *
     * @param gui the gui to save
     */
    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    /**
     * Set the text displaying the user nickname and his points
     */
    public void setPlayerText(String nickname, int points) {
        String text = nickname + " - Total points: " + points;
        playerText.setText(text);
    }

    /**
     * Set the user personal target card
     *
     * @param personalNumber the number of the personal target card
     */
    public void setPersonalTargetCard(int personalNumber) throws URISyntaxException, IOException {
        this.personalGoalImage = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/personal/personal" + personalNumber + ".png")));
    }

    /**
     * Shows the personal target card to the user
     */
    public void showPersonalTargetCard() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/PersonalGoal.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                PersonalGoalController personalGoalController = loader.getController();
                personalGoalController.setPersonalGoal(personalGoalImage);
                stage.setResizable(false);
                stage.setTitle("My Shelfie - Your Personal Goal!");
                stage.show();
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
        });
    }

    /**
     * Set the common target card(s) of the game
     *
     * @param commonTargetCardName the name of the common target card
     * @throws FileNotFoundException if the image file for the common goal or token is not found
     * @throws URISyntaxException if there is a syntax error in the URI of the image file
     */
    public void setCommonTargetCard(List<String> commonTargetCardName) throws URISyntaxException, FileNotFoundException {

        this.commonGoalNames = commonTargetCardName;
        this.onlyOneCommon = this.commonGoalNames.size() == 1;
    }

    /**
     * Shows the common target card(s) of the game
     */
    public void showCommonTargetCard() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/CommonGoal.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                CommonGoalController commonGoalController = loader.getController();
                commonGoalController.setCommons(onlyOneCommon, commonGoalNames, commonsToTokens);
                stage.setResizable(false);
                stage.setTitle("My Shelfie - Common goals!");
                stage.show();
            } catch (IOException | URISyntaxException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
        });
    }

    /**
     * Send a message in the chat
     */
    public void sendInChat() {
        String message = chatMessageTextArea.getText();
        if(message != null && !message.equals("")) {
            gui.sendMessage(message);
            chatMessageTextArea.setText("");
        }
    }

    /**
     * Shows other players shelfs
     */
    public void showOtherPlayers() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/OtherPlayers.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                OtherPlayersController otherPlayersController = loader.getController();
                otherPlayersController.setGsc(this);
                otherPlayersController.setParameters(nicknameToShelfMap, nicknameToPointsMap, playerNickname);
                stage.setResizable(false);
                stage.setTitle("My Shelfie - Other players!");
                stage.show();
            } catch (IOException | URISyntaxException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
        });
    }

    /**
     * Add a message to the chat
     *
     * @param message the message to send
     * @param sender the sender of the message
     * @param localDateTime the date and time of when the message was sent
     */
    public void addMessageInChat(String message, String sender, String localDateTime) {
        String messageText = localDateTime + "\n" + sender + ": " + message;
        Label newMsg = new Label(messageText);
        newMsg.setMaxWidth(MAX_CHAT_MSG);
        newMsg.setTextFill(Color.WHITE);
        newMsg.setTranslateX(OFFSET);
        newMsg.setTranslateY(OFFSET);
        newMsg.wrapTextProperty().set(true);
        Platform.runLater(() -> {
            chatVBox.getChildren().add(newMsg);
        });
    }

    /**
     * Initialize the game screen
     *
     * @param board array of item representing the board
     * @param bitMask array of boolean representing the available spots on the board
     */
    public void setBoardItems(Item[][] board, boolean[][] bitMask) throws IOException, URISyntaxException {
        this.boardMatrx = board;
        for(int i = 0; i < BOARD_DIM; i++) {
            for(int j = 0; j < BOARD_DIM; j++) {
                if(board[i][j] != null && bitMask[i][j]) {
                    ImageView imgv = new ImageView(randomItemImageByColors(board[i][j].getColor()));
                    imgv.setFitHeight(ITEM_DIM);
                    imgv.setFitWidth(ITEM_DIM);
                    imgv.setTranslateX(ITEM_OFFSET_LEFT);
                    imgv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            for(Node n : boardGridPane.getChildren()) {
                                if(n == mouseEvent.getSource()) {
                                    int row = GridPane.getRowIndex(n);
                                    int col = GridPane.getColumnIndex(n);
                                    if(n.getOpacity() == 1.0 && (takeableItems == null || takeableItems[row][col])) {
                                        addInSelected(n);
                                    }
                                    else if(n.getOpacity() == 0.5) {
                                        removeFromSelected(n);
                                    }
                                    break;
                                }
                            }
                        }
                    });
                    boardGridPane.add(imgv, j , i);
                }
            }
        }
    }

    /**
     * Gives back a random item image based on the color given
     * @param color color representing the item
     */
    public Image randomItemImageByColors(ItemColor color) throws URISyntaxException, IOException { // todo PROBLEMI
        Random random = new Random();
        int rand = random.nextInt(3);
        Image img = null;
        switch (color) {
            case BLUE -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/b" + rand + ".png")));
            }
            case GREEN -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/g" + rand + ".png")));
            }
            case YELLOW -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/y" + rand + ".png")));
            }
            case WHITE -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/w" + rand + ".png")));
            }
            case PINK -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/p" + rand + ".png")));
            }
            case LIGHT_BLUE -> {
                img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/lb" + rand + ".png")));
            }
        }
        return img;
    }

    /**
     * Sets parameters for the player
     *
     * @param nicknameToShelfMap map with each user nickname and his shelf
     * @param nicknameToPointsMap map with each user nickname and his points
     * @param playerNickname nickname of the player
     * @param firstPlayer nickname of the first player
     */
    public void setOtherPlayersParameters(Map<String, Item[][]> nicknameToShelfMap, Map<String, Integer> nicknameToPointsMap, String playerNickname, String firstPlayer) {
        this.nicknameToShelfMap = nicknameToShelfMap;
        this.nicknameToPointsMap = nicknameToPointsMap;
        this.playerNickname = playerNickname;
        this.firstPlayer = firstPlayer;
    }

    /**
     * Method to add in to the selected items
     *
     * @param n node selected
     */
    public void addInSelected(Node n) {
        int col = gui.getPositionPickedSize();
        if(col < 3 && gui.isYourTurn() && n.getOpacity() == 1.0) {
            ImageView selected = (ImageView) n;
            ImageView newImgv = new ImageView(selected.getImage());
            selected.setOpacity(0.5);
            newImgv.setFitHeight(PICKED_IT_DIM);
            newImgv.setFitWidth(PICKED_IT_DIM);
            newImgv.setTranslateY(ITEM_OFFSET_LEFT);
            newImgv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for(Node n : pickedItemsGridPane.getChildren()) {
                        if(n == mouseEvent.getSource()) {
                            swapPickedItems(n);
                        }
                    }
                }
            });
            pickedItemsGridPane.add(newImgv, col, 0);
            int pickedRow = GridPane.getRowIndex(n);
            int pickedCol = GridPane.getColumnIndex(n);
            int[] pickedPos = new int[2];
            pickedPos[0] = pickedRow;
            pickedPos[1] = pickedCol;
            gui.insertInPositionPicked(pickedPos);
        }
    }

    /**
     * Method to remove from the selected items
     *
     * @param n node selected
     */
    public void removeFromSelected(Node n) {
        int col = 0;

        for(Node node : pickedItemsGridPane.getChildren()) {
            ImageView selected = (ImageView) n;
            ImageView pickedSelected = (ImageView) node;
            if(selected.getImage() == pickedSelected.getImage()) {
                col = GridPane.getColumnIndex(node);
            }
        }
        gui.removeInPositionPicked(col);
        int maxIndex = gui.getPositionPickedSize();
        for(int i = col; i < maxIndex; i++) {
            swapTwoImageViewInGridPane(getGridPaneChildrenByCoordinate(pickedItemsGridPane, 0, i), getGridPaneChildrenByCoordinate(pickedItemsGridPane, 0, i+1));
        }
        Node toRemove = null;
        for(Node node : pickedItemsGridPane.getChildren()) {
            if(GridPane.getColumnIndex(node) == maxIndex) {
                toRemove = node;
            }
        }
        pickedItemsGridPane.getChildren().remove(toRemove);
        n.setOpacity(1.0);
    }

    /**
     * Sets up the shelf for the player in the GUI
     *
     * @throws FileNotFoundException if the image file for the common goal or token is not found
     * @throws URISyntaxException if there is a syntax error in the URI of the image file
     */
    public void setupPlayerShelf() throws URISyntaxException, FileNotFoundException {
        for(int i = 0; i < SHELF_ROWS; i++) {
            for(int j = 0; j < SHELF_COL; j++) {
                Image img = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/items/b0.png")));
                ImageView imgv = new ImageView(img);
                imgv.setFitWidth(SHELF_ITEM_DIM);
                imgv.setFitHeight(SHELF_ITEM_DIM);
                imgv.setTranslateX(SHELF_ITEM_OFFSET);
                imgv.setOpacity(0.0);
                imgv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        for(Node n : shelfGridPane.getChildren()) {
                            int col = GridPane.getColumnIndex(n);
                            if(n == mouseEvent.getSource()) {
                                if(gui.isYourTurn() && gui.getPositionPickedSize() > 0 && gui.columnHasEnoughSpace(col)) {
                                    gui.sendMove(col);
                                    pickedItemsGridPane.getChildren().clear();
                                }
                                break;
                            }
                        }
                    }
                });
                shelfGridPane.add(imgv, j, i);
            }
        }
    }

    /**
     * Sets up the "your turn" panel
     * @param name name of the current user
     * @param set boolean indicating if it is the player's turn
     * @param waitForOtherPlayers boolean indicating if the game is waiting for other players
     */
    public void setYourTurnPane(boolean set, String name, boolean waitForOtherPlayers) {
        if(!waitForOtherPlayers) {
            if (set) {
                turnLabel.setText("It's your turn!");
            } else {
                turnLabel.setText("It's " + name + " turn!");
            }
        }
        else {
            turnLabel.setText("");
        }
    }

    /**
     * Swaps the picked items in the GUI
     *
     * @param n node of the items to switch
     */
    public void swapPickedItems(Node n) {
        if(swapCols.size() == 0) {
            swapCols.add(n);
        }
        else {
            swapCols.add(n);
            ImageView firstImgv = (ImageView) swapCols.get(0);
            ImageView secondImgv = (ImageView) swapCols.get(1);
            Image tmpImage = firstImgv.getImage();
            firstImgv.setImage(secondImgv.getImage());
            secondImgv.setImage(tmpImage);
            gui.swapColsGUI(swapCols);
            swapCols = new ArrayList<>(2);
        }
    }

    /**
     * Gets the index of the column to swap given the node
     * @param n
     * @return
     */
    public int getSwapColIndex(Node n) {
        return GridPane.getColumnIndex(n);
    }

    /**
     * Clear the board
     */
    public void clearBoard() {
        boardGridPane.getChildren().clear();
    }

    /**
     * Sets up the personal shelf
     *
     * @param shelf the shelf to be set up
     * @throws IOException
     * @throws URISyntaxException
     */
    public void setPersonalShelf(Item[][] shelf) throws IOException, URISyntaxException {
        for(int i = 0; i < SHELF_ROWS; i++) {
            for(int j = 0; j < SHELF_COL; j++) {
                if(shelf[i][j] != null) {
                    for(Node n : shelfGridPane.getChildren()) {
                        if(GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == j) {
                            ImageView imgv = (ImageView) n;
                            imgv.setImage(randomItemImageByColors(shelf[i][j].getColor()));
                            imgv.setOpacity(1.0);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Warns the user that an incorrect move was made
     */
    public void incorrectMove() {
        for(Node n: boardGridPane.getChildren()) {
            if(n.getOpacity() != 1.0) {
                n.setOpacity(1.0);
            }
        }
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/IncorrectMove.fxml"));
            Stage incorrectMoveStage = new Stage();
            try {
                incorrectMoveStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            incorrectMoveStage.setTitle("INCORRECT MOVE!");
            incorrectMoveStage.setResizable(false);
            incorrectMoveStage.initModality(Modality.APPLICATION_MODAL);
            incorrectMoveStage.showAndWait();
        });
    }

    /**
     * Warns the user that there's been a wrong receiver
     */
    public void wrongReceiver() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/WrongReceiver.fxml"));
            Stage wrongReceiverStage = new Stage();
            try {
                wrongReceiverStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            wrongReceiverStage.setTitle("WRONG RECEIVER!");
            wrongReceiverStage.setResizable(false);
            wrongReceiverStage.initModality(Modality.APPLICATION_MODAL);
            wrongReceiverStage.showAndWait();
        });
    }

    /**
     * Set the chair as visible in the GUI
     */
    public void setChair() {
        chairImageView.setDisable(false);
        chairImageView.setVisible(true);
    }

    /**
     * Shows the tokens of the user
     */
    public void yourTokens() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/YourTokens.fxml"));
            Stage yourTokensStage = new Stage();
            try {
                yourTokensStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            YourTokensController yourTokensController = loader.getController();
            try {
                yourTokensController.setupTokens(playerTokens, endGameToken);
            } catch (URISyntaxException | FileNotFoundException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            yourTokensStage.setTitle("My Shelfie - Your tokens!");
            yourTokensStage.setResizable(false);
            yourTokensStage.show();
        });
    }

    /**
     * Removes the end game token
     */
    public void removeEndGameToken() {
        endGameTokenImageView.setDisable(true);
        endGameTokenImageView.setVisible(false);
    }

    /**
     * Sets the tokens of the player
     *
     * @param commonsToTokens map of the common cards and their tokens
     * @param playerTokens list of the player's tokens
     */
    public void setTokens(Map<String, List<ScoringToken>> commonsToTokens, List<ScoringToken> playerTokens) {
        this.commonsToTokens = commonsToTokens;
        this.playerTokens = playerTokens;
    }

    /**
     * Sets the endgame token based on the given token
     * @param endGameToken token to be set
     */
    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }

    /**
     * Gets a children in the grid pane using the coordinates given
     *
     * @param gp gridpane to analyze
     * @param row row coordinate
     * @param col column coordinate
     * @return the node of the child
     */
    public Node getGridPaneChildrenByCoordinate(GridPane gp, int row, int col) {
        for(Node n : gp.getChildren()) {
            if(GridPane.getColumnIndex(n) == col && GridPane.getRowIndex(n) == row) {
                return n;
            }
        }
        return null;
    }

    /**
     * Swaps two image views in the grid pane
     *
     * @param one node of the first image view to swap
     * @param two node of the second image view to swap
     */
    public void swapTwoImageViewInGridPane(Node one, Node two) {
        ImageView oneImgv = (ImageView) one;
        ImageView twoImgv = (ImageView) two;
        Image tmp = oneImgv.getImage();
        oneImgv.setImage(twoImgv.getImage());
        twoImgv.setImage(tmp);
    }

    /**
     * Notify that the player has disconnected
     *
     * @param nickname nickname of the disconnected user
     */
    public void playerDisconnected(String nickname) {
        if(!notifyIsOn) {
            notifyIsOn = true;
            Platform.runLater(() -> notificationLabel.setText(nickname + "\nhas disconnected!"));
            FadeTransition fadeIn = new FadeTransition(Duration.millis(TRANSITION_DURATION_EFF), notificationAnchorPane);
            fadeIn.setFromValue(OPACITY_LOW);
            fadeIn.setToValue(OPACITY_HIGH);
            fadeIn.play();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(TRANSITION_DURATION_EFF), notificationAnchorPane);
                    fadeOut.setFromValue(OPACITY_HIGH);
                    fadeOut.setToValue(OPACITY_LOW);
                    fadeOut.play();
                    notifyIsOn = false;
                    timer.cancel();
                }
            }, NOTIFY_DURATION);
        }
    }

    /**
     * Notify that the player has reconnected
     *
     * @param nickname nickname of the disconnected user
     */
    public void playerReconnected(String nickname) {
        if(!notifyIsOn) {
            notifyIsOn = true;
            Platform.runLater(() -> notificationLabel.setText(nickname + "\nhas reconnected!"));
            FadeTransition fadeIn = new FadeTransition(Duration.millis(TRANSITION_DURATION_EFF), notificationAnchorPane);
            fadeIn.setFromValue(OPACITY_LOW);
            fadeIn.setToValue(OPACITY_HIGH);
            fadeIn.play();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(TRANSITION_DURATION_EFF), notificationAnchorPane);
                    fadeOut.setFromValue(OPACITY_HIGH);
                    fadeOut.setToValue(OPACITY_LOW);
                    fadeOut.play();
                    notifyIsOn = false;
                    timer.cancel();
                }
            }, NOTIFY_DURATION);
        }
    }

    /**
     * Sets the takeable items on the gridpane
     *
     * @param takeableItems boolean array representing the position of the takeable items
     */
    public void setTakeableItems(boolean[][] takeableItems) {
        this.takeableItems = takeableItems;
        for(int i = 0; i < BOARD_DIM; i++) {
            for(int j = 0; j < BOARD_DIM; j++) {
                if(takeableItems[i][j]) {
                    for(Node n : boardGridPane.getChildren()) {
                        if(GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == j) {
                            DropShadow takeableEff = new DropShadow();
                            takeableEff.setWidth(EFFECT_DIM);
                            takeableEff.setHeight(EFFECT_DIM);
                            takeableEff.setColor(EFFECT_COL);
                            n.setEffect(takeableEff);
                        }
                    }
                }
            }
        }
    }

    /**
     * Open the menu in the GUI
     */
    public void openMenu() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/Menu.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            MenuController menuController = loader.getController();
            menuController.setGui(gui);
            menuController.setMenuStage(stage);
            stage.setResizable(false);
            stage.setTitle("My Shelfie - Main menu!");
            stage.show();
        });
    }

    /**
     * Shows the "wait for other players" page
     */
    public void waitForOtherPlayers() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/LastUserAlreadyMadeYourMove.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                System.err.println("A crash occurred when loading the scene, please restart the software!");
            }
            stage.setTitle("My Shelfie - Wait for other players!");
            stage.setResizable(false);
            stage.show();
        });
    }

}
