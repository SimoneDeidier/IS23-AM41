package it.polimi.ingsw.client.clientontroller.connection;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.clientontroller.ClientController;
import it.polimi.ingsw.client.clientontroller.SerializeDeserialize;
import it.polimi.ingsw.client.view.GraphicUserInterface;
import it.polimi.ingsw.client.view.UserInterface;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionTCP implements Connection {


    private boolean closeConnection = false;
    private Socket socket;
    private PrintWriter socketOut;
    private Scanner socketIn;
    private final SerializeDeserialize serializeDeserialize;
    private UserInterface userInterface  = null;
    private Thread userInterfaceThread = null;


    public ConnectionTCP(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serializeDeserialize = new SerializeDeserialize(this);
    }

    @Override
    public void startConnection(String uiType) {
        Thread socketReader = new Thread(() -> {
            while(!closeConnection) {
                String inMsg;
                while ((inMsg = socketIn.nextLine()) != null) {
                    serializeDeserialize.deserialize(inMsg);
                }
            }
        });
        socketReader.start();
        ClientController clientController = new ClientController();

        switch (uiType) {
            case "gui" -> userInterface = new GraphicUserInterface();
            case "tui" -> {}
        }
        userInterfaceThread = new Thread((Runnable) userInterface);
        userInterfaceThread.start();
        try {
            socketReader.join();
            userInterfaceThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        socketOut.close();
        socketIn.close();
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getSocketOut() {
        return socketOut;
    }

    public void closeConnection() {
        this.closeConnection = true;
    }

}
