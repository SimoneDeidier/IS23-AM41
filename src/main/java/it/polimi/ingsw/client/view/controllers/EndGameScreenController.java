package it.polimi.ingsw.client.view.controllers;

import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.messages.NewView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.*;

public class EndGameScreenController {

    @FXML
    private Label winsLabel;
    @FXML
    private Label firstLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private Label thirdLabel;
    @FXML
    private Label fourthLabel;

    private GraphicUserInterface gui = null;

    public void setParameters(NewView newView) {
        Map<String, Integer> pointsMap = newView.getNicknameToPointsMap();
        List<String> orderedPlayers = getAscendingPointsFromMap(pointsMap);
        int size = orderedPlayers.size();
        winsLabel.setText(orderedPlayers.get(size - 1) + " WINS!");
        firstLabel.setText("1° - " + orderedPlayers.get(size - 1) + " - " + pointsMap.get(orderedPlayers.get(size - 1)) + " points");
        secondLabel.setText("2° - " + orderedPlayers.get(size - 2) + " - " + pointsMap.get(orderedPlayers.get(size - 2)) + " points");
        if(size > 2) {
            thirdLabel.setText("3° - " + orderedPlayers.get(size - 3) + " - " + pointsMap.get(orderedPlayers.get(size - 3)) + " points");
        }
        if(size == 4) {
            fourthLabel.setText("4° - " + orderedPlayers.get(0) + " - " + pointsMap.get(orderedPlayers.get(0)) + " points");
        }
    }

    private List<String> getAscendingPointsFromMap(Map<String, Integer> pointsMap) {
        List<String> playerList = new ArrayList<>();
        playerList.addAll(pointsMap.keySet());
        List<String> orderPlayerList=new ArrayList<>();//list with first player = max number of points

        orderPlayerList.add(playerList.get(playerList.size()-1));
        if(pointsMap.get(playerList.get(playerList.size()-2))> pointsMap.get(orderPlayerList.get(0)))
            orderPlayerList.add(0, playerList.get(playerList.size()-2));
        else
            orderPlayerList.add(playerList.get(playerList.size()-2));

        if(playerList.size()==3){
            if(pointsMap.get(playerList.get(0))>pointsMap.get(orderPlayerList.get(0)))
                orderPlayerList.add(0,playerList.get(0));
            else if(pointsMap.get(playerList.get(0))>pointsMap.get(orderPlayerList.get(1)))
                orderPlayerList.add(1, playerList.get(0));
            else
                orderPlayerList.add(playerList.get(0));
        }

        if(playerList.size()==4){
            if(pointsMap.get(playerList.get(playerList.size()-3))>pointsMap.get(orderPlayerList.get(0)))
                orderPlayerList.add(0,playerList.get(playerList.size()-3));
            else if(pointsMap.get(playerList.get(playerList.size()-3))>pointsMap.get(orderPlayerList.get(1)))
                orderPlayerList.add(1, playerList.get(playerList.size()-3));
            else
                orderPlayerList.add(playerList.get(playerList.size()-3));
            if(pointsMap.get(playerList.get(0))>pointsMap.get(orderPlayerList.get(0)))
                orderPlayerList.add(0,playerList.get(0));
            else if(pointsMap.get(playerList.get(0))>pointsMap.get(orderPlayerList.get(1)))
                orderPlayerList.add(1,playerList.get(0));
            else if(pointsMap.get(playerList.get(0))>pointsMap.get(orderPlayerList.get(2)))
                orderPlayerList.add(2,playerList.get(0));
            else orderPlayerList.add(playerList.get(0));
        }
        Collections.reverse(orderPlayerList);
        return orderPlayerList;
    }

    public void exit() {
        gui.exit();
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

}
