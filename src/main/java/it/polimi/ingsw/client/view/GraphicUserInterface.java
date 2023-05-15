package it.polimi.ingsw.client.view;

public class GraphicUserInterface extends UserInterface {

    @Override
    public void start() {
        new Thread(() -> {
            System.out.println("Started GUI");
        }).start();
    }

    @Override
    public void askNickname() {
        super.askNickname();
    }
}
