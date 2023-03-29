package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ServerInitState extends GameState {

    @Override
    public int getAvailableSlot(int maxPlayerNumber, int listSize) {
        return -1;
    }

    @Override
    public void addPlayer(Player player, ArrayList<Player> playerList) {
        // come gestiamo l'inserimento del primo giocatore?
        //Serve un check che dica se passare in waitingForPlayer nel caso in cui
        //il nickname del giocatore iniziale che sta provando a collegarsi sia diverso
        // da quelli salvati nel json, ed invece che vada in waiting forSaved nel caso contrario
        //Cambia molto perchè per esempio se dobbiamo andare in waitingForSaved non
        //serve chiedere per quanti giocatori vuole la partita
        playerList.add(player);
    }

    @Override
    public void setupGame() {
        // in server initialization state we initialize only the items bag
        ItemsBag itemsBag = ItemsBag.getItemsBag();
        //todo e mo?
    }
}
