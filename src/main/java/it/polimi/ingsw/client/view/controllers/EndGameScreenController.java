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
        List<Integer> orderedPoints = getAscendingPointsFromMap(pointsMap);
        List<String> orderedPlayers = getAscendingPlayersFromMap(pointsMap, orderedPoints);
        int size = orderedPlayers.size();
        winsLabel.setText(orderedPlayers.get(size - 1) + " WINS!");
        firstLabel.setText("1° - " + orderedPlayers.get(size - 1) + " - " + orderedPoints.get(size - 1) + " points");
        secondLabel.setText("2° - " + orderedPlayers.get(size - 2) + " - " + orderedPoints.get(size - 2) + " points");
        if(size > 2) {
            thirdLabel.setText("3° - " + orderedPlayers.get(size - 3) + " - " + orderedPoints.get(size - 3) + " points");
        }
        if(size == 4) {
            fourthLabel.setText("4° - " + orderedPlayers.get(0) + " - " + orderedPoints.get(0) + " points");
        }
    }

    public void exit() {
        gui.exit();
    }

    public void setGui(GraphicUserInterface gui) {
        this.gui = gui;
    }

    public List<Integer> getAscendingPointsFromMap(Map<String, Integer> map) {
        List<Integer> res = new ArrayList<>(4);
        for(String s : map.keySet()) {
            res.add(map.get(s));
        }
        Collections.sort(res);
        return res;
    }

    public List<String> getAscendingPlayersFromMap(Map<String, Integer> map, List<Integer> ascPoints) {
        List<String> res = new ArrayList<>(4);
        for(Integer i : ascPoints) {
            for(String s : map.keySet()) {
                if(Objects.equals(map.get(s), i) && !res.contains(s)) {
                    res.add(s);
                    break;
                }
            }
        }
        return res;
    }

}
