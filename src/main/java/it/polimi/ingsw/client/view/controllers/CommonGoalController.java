package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.server.model.tokens.ScoringToken;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class responsible for the display of common goals in the client view.
 */
public class CommonGoalController {

    @FXML
    private AnchorPane mainAnchorPane;

    // one

    private static final double WIDTH_ONE = 333.0;
    private static final double HEIGHT_ONE = 220.0;
    private static final double OFFSET_X_ONE = 153.0;
    private static final double OFFSET_Y_ONE = 69.0;
    private static final double TOKEN_DIM_ONE = 109.0;
    private static final double TOKEN_OFF_X_ONE = 341.0;
    private static final double TOKEN_OFF_Y_ONE = 117.0;

    // two

    private static final double WIDTH_TWO = 274.0;
    private static final double HEIGHT_TWO = 181.0;
    private static final double OFFSET_Y_TWO = 81.0;
    private static final double OFFSET_X_TWO_LEFT = 31.0;
    private static final double OFFSET_X_TWO_RIGHT = 335.0;
    private static final double TOKEN_DIM_TWO = 84.0;
    private static final double TOKEN_OFF_X_TWO_LEFT = 189.0;
    private static final double TOKEN_OFF_X_TWO_RIGHT = 493.0;
    private static final double TOKEN_OFF_Y_TWO = 123.0;

    // common vals

    private static final double TOKEN_ROTATE = -8.0;

    /**
     * Sets the common goals on the view.
     *
     * @param isOneCommon a boolean indicating if there is one or two common goals
     * @param commons a list of strings representing the names of the common goals
     * @param map a map with all common goals and their ScoringTokens
     * @throws FileNotFoundException if the image file for the common goal or token is not found
     * @throws URISyntaxException if there is a syntax error in the URI of the image file
     */
    public void setCommons(boolean isOneCommon, List<String> commons, Map<String, List<ScoringToken>> map) throws FileNotFoundException, URISyntaxException {

        if(isOneCommon) {
            ImageView imageView = new ImageView(getCommonImageByName(commons.get(0)));
            imageView.setFitWidth(WIDTH_ONE);
            imageView.setFitHeight(HEIGHT_ONE);
            imageView.setTranslateX(OFFSET_X_ONE);
            imageView.setTranslateY(OFFSET_Y_ONE);
            mainAnchorPane.getChildren().add(imageView);
            Image img = getMaxTokenValueByCardNumber(commons.get(0), map);
            if(img != null) {
                ImageView tkImgv = new ImageView(img);
                tkImgv.setFitWidth(TOKEN_DIM_ONE);
                tkImgv.setFitHeight(TOKEN_DIM_ONE);
                tkImgv.setRotate(TOKEN_ROTATE);
                tkImgv.setTranslateX(TOKEN_OFF_X_ONE);
                tkImgv.setTranslateY(TOKEN_OFF_Y_ONE);
                mainAnchorPane.getChildren().add(tkImgv);
            }
        }
        else {
            ImageView imageView1 = new ImageView(getCommonImageByName(commons.get(0)));
            imageView1.setFitWidth(WIDTH_TWO);
            imageView1.setFitHeight(HEIGHT_TWO);
            imageView1.setTranslateY(OFFSET_Y_TWO);
            imageView1.setTranslateX(OFFSET_X_TWO_LEFT);
            mainAnchorPane.getChildren().add(imageView1);
            Image img = getMaxTokenValueByCardNumber(commons.get(0), map);
            if(img != null) {
                ImageView tkImgv1 = new ImageView(img);
                tkImgv1.setFitWidth(TOKEN_DIM_TWO);
                tkImgv1.setFitHeight(TOKEN_DIM_TWO);
                tkImgv1.setRotate(TOKEN_ROTATE);
                tkImgv1.setTranslateX(TOKEN_OFF_X_TWO_LEFT);
                tkImgv1.setTranslateY(TOKEN_OFF_Y_TWO);
                mainAnchorPane.getChildren().add(tkImgv1);
            }
            ImageView imageView2 = new ImageView(getCommonImageByName(commons.get(1)));
            imageView2.setFitWidth(WIDTH_TWO);
            imageView2.setFitHeight(HEIGHT_TWO);
            imageView2.setTranslateY(OFFSET_Y_TWO);
            imageView2.setTranslateX(OFFSET_X_TWO_RIGHT);
            mainAnchorPane.getChildren().add(imageView2);
            Image img1 = getMaxTokenValueByCardNumber(commons.get(1), map);
            if(img1 != null) {
                ImageView tkImgv2 = new ImageView(img1);
                tkImgv2.setFitWidth(TOKEN_DIM_TWO);
                tkImgv2.setFitHeight(TOKEN_DIM_TWO);
                tkImgv2.setRotate(TOKEN_ROTATE);
                tkImgv2.setTranslateX(TOKEN_OFF_X_TWO_RIGHT);
                tkImgv2.setTranslateY(TOKEN_OFF_Y_TWO);
                mainAnchorPane.getChildren().add(tkImgv2);
            }
        }
    }

    /**
     * Return the image of a common goal based on its name
     * @param name the name of the common card
     * @throws FileNotFoundException if the image file for the common goal or token is not found
     * @throws URISyntaxException if there is a syntax error in the URI of the image file
     */
    public Image getCommonImageByName(String name) throws URISyntaxException, FileNotFoundException {
        return new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/commons/" + name + ".jpg")));
    }

    /**
     * Return the max token value of a common goal based on its name
     *
     * @param name the name of the common card
     * @param map a map with all common goals and their ScoringTokens
     * @throws FileNotFoundException if the image file for the common goal or token is not found
     * @throws URISyntaxException if there is a syntax error in the URI of the image file
     */
    public Image getMaxTokenValueByCardNumber(String name, Map<String, List<ScoringToken>> map) throws URISyntaxException, FileNotFoundException {
        for(String s : map.keySet()) {
            if(Objects.equals(s, name)) {
                List<ScoringToken> list = map.get(s);
                int max = -1;
                for(ScoringToken tk : list) {
                    if(tk.isTakeable()) {
                        if (tk.getValue() > max) {
                            max = tk.getValue();
                        }
                    }
                }
                if(max != -1) {
                    return new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/tokens/scoring" + max + ".jpg")));
                }
            }
        }
        return null;
    }


}
