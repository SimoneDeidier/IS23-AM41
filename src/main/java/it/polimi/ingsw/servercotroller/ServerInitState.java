package it.polimi.ingsw.servercotroller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.ItemsBag;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.servercotroller.GameState;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ServerInitState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return -1;
    }

    @Override
    public void addPlayer(Player player, List<Player> playerList) throws URISyntaxException, IOException {
        // come gestiamo l'inserimento del primo giocatore?
        //Serve un check che dica se passare in waitingForPlayer nel caso in cui
        //il nickname del giocatore iniziale che sta provando a collegarsi sia diverso
        // da quelli salvati nel json, ed invece che vada in waiting forSaved nel caso contrario
        //Cambia molto perchè per esempio se dobbiamo andare in waitingForSaved non
        //serve chiedere per quanti giocatori vuole la partita
        Gson gson = new Gson();
        File jsonFile = new File(ClassLoader.getSystemResource("OldGame.json").toURI());
        String jsonString = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);



        playerList.add(player);
    }

    @Override
    public void setupGame(int maxPlayerNumber) {
        //e invece no itemsBag, la fa board
    }
}
