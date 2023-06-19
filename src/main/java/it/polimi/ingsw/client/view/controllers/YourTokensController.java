package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.server.model.tokens.EndGameToken;
import it.polimi.ingsw.server.model.tokens.ScoringToken;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YourTokensController {

    @FXML
    private GridPane commonsGridPane;
    @FXML
    protected GridPane endGridPane;

    private static final double TOKEN_DIM = 91.0;
    private static final double TOKEN_OFFSET = 5.0;


    public void setupTokens(List<ScoringToken> list, EndGameToken endGameToken) throws URISyntaxException, FileNotFoundException {
        if(endGameToken != null) {
            ImageView egtImgv = new ImageView(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/tokens/endgametoken.jpg"))));
            egtImgv.setFitHeight(TOKEN_DIM);
            egtImgv.setFitWidth(TOKEN_DIM);
            egtImgv.setTranslateX(TOKEN_OFFSET);
            endGridPane.add(egtImgv, 0, 0);
        }
        int count = 0;
        for(ScoringToken s : list) {
            ImageView scoringImgv = new ImageView(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("images/tokens/scoring" + s.getValue() + ".jpg"))));
            scoringImgv.setFitHeight(TOKEN_DIM);
            scoringImgv.setFitWidth(TOKEN_DIM);
            scoringImgv.setTranslateX(TOKEN_OFFSET);
            commonsGridPane.add(scoringImgv, count, 0);
            count++;
        }
    }

}
