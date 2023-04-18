package it.polimi.ingsw.client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private static final String IP = "localhost";
    private static final int PORT = 8888;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(IP, PORT);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
