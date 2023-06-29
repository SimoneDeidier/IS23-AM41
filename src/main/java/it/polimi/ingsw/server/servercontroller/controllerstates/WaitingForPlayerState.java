package it.polimi.ingsw.server.servercontroller.controllerstates;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.boards.BoardFactory;
import it.polimi.ingsw.server.model.boards.FourPlayersBoard;
import it.polimi.ingsw.server.model.boards.ThreePlayersBoard;
import it.polimi.ingsw.server.model.boards.TwoPlayersBoard;
import it.polimi.ingsw.server.model.commons.*;
import it.polimi.ingsw.server.servercontroller.GameController;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * after ServerInitState, the controller enters this state if the first player's nickname wasn't in the saved game
 */
public class WaitingForPlayerState implements GameState {

    /**
     *
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return how many more places are available for this game. It returns -2 if the first player still hasn't
     * finished setting up the parameters for the game
     */
    @Override
    public int getAvailableSlot(int maxPlayerNumber, List<Player> playerList) {
        if(maxPlayerNumber == 0) {
            return -2;
        }
        else return maxPlayerNumber - playerList.size();
    }

    /**
     * generates a random PersonalTargetCard for each Player, making sure two Players doesn't have the same card
     * @param playerList is passed to make the check that no other Player have the same PersonalTargetCard that is being generated at the moment
     * @return a PersonalTargetCard for the Player
     * @throws IOException because it reads from a JSON file to create the cards
     * @throws URISyntaxException because it reads from a JSON file to create the cards
     */
    public PersonalTargetCard generateRandomPersonal(List<Player> playerList) throws IOException, URISyntaxException {
        Random random = new Random();
        int personalCode= (random.nextInt(12));
        boolean isAlreadyPresent = false;
        for(Player playerInFor: playerList){
            if(playerInFor.getPersonalTargetCard()!=null && playerInFor.getPersonalTargetCard().getPersonalNumber() == personalCode) {
                isAlreadyPresent = true;
                break;
            }
        }
        while(isAlreadyPresent) {
            isAlreadyPresent = false;
            personalCode = (random.nextInt(12));
            for (Player playerInFor : playerList) {
                if (playerInFor.getPersonalTargetCard() != null && playerInFor.getPersonalTargetCard().getPersonalNumber() == personalCode) {
                    isAlreadyPresent = true;
                    break;
                }
            }
        }
        return new PersonalTargetCard( personalCode);
    }

    /**
     * generates one or two CommonTargetCard for the game
     * @param onlyOneCommonCard it's what is read to understand whether to create one or two CommonTargetCard (one if true, false otherwise)
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return a list containing all CommonTargetCards needed for the match
     */
    public List<CommonTargetCard> generateRandomCommonCards(boolean onlyOneCommonCard,int maxPlayerNumber) {
        List<CommonTargetCard> list =new ArrayList<>();
        list.add(getRandomCommon(maxPlayerNumber));
        if(onlyOneCommonCard){
            return list;
        }
        CommonTargetCard common2= getRandomCommon(maxPlayerNumber);
        while(list.get(0).getClass().equals(common2.getClass())){
            common2=getRandomCommon(maxPlayerNumber);
        }
        list.add(common2);
        return list;
    }

    /**
     * helps the method generateRandomCommonCards, providing a randomly selected CommonTargetCard
     * @param maxPlayerNumber the maximum number of players allowed in the game, provided here to assign to the CommontargetCard
     *                        the right number of ScoringTokens
     * @return a randomly selected CommonTargetCard*
     */
    public CommonTargetCard getRandomCommon(int maxPlayerNumber){
        Random random=new Random();
        switch (random.nextInt(12)) {
            case 0 -> {
                return new CommonDiagonal(maxPlayerNumber);
            }
            case 1 -> {
                return new CommonEightSame(maxPlayerNumber);
            }
            case 2 -> {
                return new CommonFourCorners(maxPlayerNumber);
            }
            case 3 -> {
                return new CommonFourGroupsOfFour(maxPlayerNumber);
            }
            case 4 -> {
                return new CommonFourRows(maxPlayerNumber);
            }
            case 5 -> {
                return new CommonSixGroupsOfTwo(maxPlayerNumber);
            }
            case 6 -> {
                return new CommonStairway(maxPlayerNumber);
            }
            case 7 -> {
                return new CommonThreeColumns(maxPlayerNumber);
            }
            case 8 -> {
                return new CommonTwoColumns(maxPlayerNumber);
            }
            case 9 -> {
                return new CommonTwoRows(maxPlayerNumber);
            }
            case 10 -> {
                return new CommonTwoSquares(maxPlayerNumber);
            }
            default -> { //case: 11
                return new CommonX(maxPlayerNumber);
            }
        }
    }

    /**
     * checks whether a nickname is available
     * @param nickname is the nickname to be checked
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @return 0 whether the nickname is not available, 1 otherwise
     */
    @Override
    public int checkNicknameAvailability(String nickname, List<Player> playerList){
        for(Player player:playerList){
            if(player.getNickname().equals(nickname)){
                return 0;
            }
        }
        return 1;
    }

    /**
     * adds a Player to the playerList
     * @param player is the Player that needs to be added
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     */
    @Override
    public void addPlayer(Player player, List<Player> playerList) {
        playerList.add(player);
    }

    /**
     * @param isOnlyOneCommon is whether the game should have one or two CommonTargetCard (one if true, two otherwise)
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return a list of the correct amount of CommonTargetCards for the game
     */
    @Override
    public List<CommonTargetCard> setupCommonList(boolean isOnlyOneCommon, int maxPlayerNumber) {
        return generateRandomCommonCards(isOnlyOneCommon, maxPlayerNumber);
    }

    /**
     * handles the creation of the Board for the game, based on the maxPlayerNumber
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return the created Board
     */
    @Override
    public BoardFactory setupBoard(int maxPlayerNumber) {
        switch (maxPlayerNumber) {
            case 2 -> {
                return new TwoPlayersBoard();
            }
            case 3 -> {
                return new ThreePlayersBoard();
            }
            case 4 -> {
                return new FourPlayersBoard();
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * checks whether the Board needs to be refilled
     * @param boardFactory the Board in the current game
     */
    @Override
    public void boardNeedsRefill(BoardFactory boardFactory) {
        boardFactory.refillBoard();
    }

    /**
     * handles the procedure to make sure that all players have access to all the objects it should have during the game
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param commonTargetCardList the list of CommonTargetCard for the current game
     * @param board the Board of the current game
     * @param controller the reference to the GameController
     */
    @Override
    public void setupPlayers(List<Player> playerList, List<CommonTargetCard> commonTargetCardList, BoardFactory board, GameController controller) {
        for(Player player: playerList){
            player.setBoard(board);
            player.setShelf(new Shelf());
            player.setCommonTargetCardList(commonTargetCardList);
            try {
                player.setPersonalTargetCard(generateRandomPersonal(playerList));
            } catch (IOException | URISyntaxException e) {
                System.err.println("It was not possible to set the personal target card to player " + player + " due to a reading error from the JSON!");
            }
        }
        Collections.shuffle(playerList);
    }

    /**
     * checks whether the game should start right now or not
     * @param playerList the list of Players currently in the game, with their connection status at the moment
     * @param maxPlayerNumber the maximum number of players allowed in the game
     * @return whether the game should start right now or not
     */
    @Override
    public boolean isGameReady(List<Player> playerList, int maxPlayerNumber){
        return playerList.size()==maxPlayerNumber;
    }


}
