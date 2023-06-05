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
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


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

    public void initialize() {
        chatVBox.setStyle("-fx-background-color: #442211;");
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public void setPlayerText(String nickname, int points) {
        String text = nickname + " - Total points: " + points;
        playerText.setText(text);
    }

    public void setPersonalTargetCard(int personalNumber) throws URISyntaxException, FileNotFoundException {
        File file = new File(ClassLoader.getSystemResource("images/personal/personal" + personalNumber + ".png").toURI());
        FileInputStream fis = new FileInputStream(file);
        this.personalGoalImage = new Image(fis);
    }

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
                throw new RuntimeException(e);
            }
        });
    }

    public void setCommonTargetCard(List<String> commonTargetCardName) throws URISyntaxException, FileNotFoundException {

        this.commonGoalNames = commonTargetCardName;
        this.onlyOneCommon = this.commonGoalNames.size() == 1;
    }

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
                e.printStackTrace();
            }
        });
    }

    public void sendInChat() {
        String message = chatMessageTextArea.getText();
        if(message != null && !message.equals("")) {
            gui.sendMessage(message);
            chatMessageTextArea.setText("");
        }
    }

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
                throw new RuntimeException(e);
            }
        });
    }

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

    public void setBoardItems(Item[][] board, boolean[][] bitMask) throws FileNotFoundException, URISyntaxException {
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

    public Image randomItemImageByColors(ItemColor color) throws URISyntaxException, FileNotFoundException {
        Random random = new Random();
        int rand = random.nextInt(3);
        File file = null;
        switch (color) {
            case BLUE -> {
                file = new File(ClassLoader.getSystemResource("images/items/b" + rand + ".png").toURI());
            }
            case GREEN -> {
                file = new File(ClassLoader.getSystemResource("images/items/g" + rand + ".png").toURI());
            }
            case YELLOW -> {
                file = new File(ClassLoader.getSystemResource("images/items/y" + rand + ".png").toURI());
            }
            case WHITE -> {
                file = new File(ClassLoader.getSystemResource("images/items/w" + rand + ".png").toURI());
            }
            case PINK -> {
                file = new File(ClassLoader.getSystemResource("images/items/p" + rand + ".png").toURI());
            }
            case LIGHT_BLUE -> {
                file = new File(ClassLoader.getSystemResource("images/items/lb" + rand + ".png").toURI());
            }
        }
        FileInputStream fis = new FileInputStream(file);
        return new Image(fis);
    }

    public void setOtherPlayersParameters(Map<String, Item[][]> nicknameToShelfMap, Map<String, Integer> nicknameToPointsMap, String playerNickname, String firstPlayer) {
        this.nicknameToShelfMap = nicknameToShelfMap;
        this.nicknameToPointsMap = nicknameToPointsMap;
        this.playerNickname = playerNickname;
        this.firstPlayer = firstPlayer;
    }

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

    public void setupPlayerShelf() throws URISyntaxException, FileNotFoundException {
        for(int i = 0; i < SHELF_ROWS; i++) {
            for(int j = 0; j < SHELF_COL; j++) {
                File file = new File(ClassLoader.getSystemResource("images/items/b0.png").toURI());
                FileInputStream fis = new FileInputStream(file);
                Image img = new Image(fis);
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

    public void setYourTurnPane(boolean set) {
        turnAnchorPane.setVisible(set);
        turnAnchorPane.setDisable(!set);
    }

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

    public int getSwapColIndex(Node n) {
        return GridPane.getColumnIndex(n);
    }

    public void clearBoard() {
        boardGridPane.getChildren().clear();
    }

    public void setPersonalShelf(Item[][] shelf) throws FileNotFoundException, URISyntaxException {
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
                e.printStackTrace();
            }
            incorrectMoveStage.setTitle("INCORRECT MOVE!");
            incorrectMoveStage.setResizable(false);
            incorrectMoveStage.initModality(Modality.APPLICATION_MODAL);
            incorrectMoveStage.showAndWait();
        });
    }

    public void wrongReceiver() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/WrongReceiver.fxml"));
            Stage wrongReceiverStage = new Stage();
            try {
                wrongReceiverStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            wrongReceiverStage.setTitle("WRONG RECEIVER!");
            wrongReceiverStage.setResizable(false);
            wrongReceiverStage.initModality(Modality.APPLICATION_MODAL);
            wrongReceiverStage.showAndWait();
        });
    }

    public void setChair() {
        chairImageView.setDisable(false);
        chairImageView.setVisible(true);
    }

    public void yourTokens() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/YourTokens.fxml"));
            Stage yourTokensStage = new Stage();
            try {
                yourTokensStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            YourTokensController yourTokensController = loader.getController();
            try {
                yourTokensController.setupTokens(playerTokens, endGameToken);
            } catch (URISyntaxException | FileNotFoundException e) {
                e.printStackTrace();
            }
            yourTokensStage.setTitle("My Shelfie - Your tokens!");
            yourTokensStage.setResizable(false);
            yourTokensStage.show();
        });
    }

    public void removeEndGameToken() {
        endGameTokenImageView.setDisable(true);
        endGameTokenImageView.setVisible(false);
    }

    public void setTokens(Map<String, List<ScoringToken>> commonsToTokens, List<ScoringToken> playerTokens) {
        this.commonsToTokens = commonsToTokens;
        this.playerTokens = playerTokens;
    }

    public void setEndGameToken(EndGameToken endGameToken) {
        this.endGameToken = endGameToken;
    }

    public Node getGridPaneChildrenByCoordinate(GridPane gp, int row, int col) {
        for(Node n : gp.getChildren()) {
            if(GridPane.getColumnIndex(n) == col && GridPane.getRowIndex(n) == row) {
                return n;
            }
        }
        return null;
    }

    public void swapTwoImageViewInGridPane(Node one, Node two) {
        ImageView oneImgv = (ImageView) one;
        ImageView twoImgv = (ImageView) two;
        Image tmp = oneImgv.getImage();
        oneImgv.setImage(twoImgv.getImage());
        twoImgv.setImage(tmp);
    }

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

    public void openMenu() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("fxml/Menu.fxml"));
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MenuController menuController = loader.getController();
            menuController.setGui(gui);
            menuController.setMenuStage(stage);
            stage.setResizable(false);
            stage.setTitle("My Shelfie - Main menu!");
            stage.show();
        });
    }
}
