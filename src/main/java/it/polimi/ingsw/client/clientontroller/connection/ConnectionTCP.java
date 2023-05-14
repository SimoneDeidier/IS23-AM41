package it.polimi.ingsw.client.clientontroller.connection;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientontroller.SerializeDeserialize;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionTCP extends Connection {


    private final Gson gson = new Gson();
    private boolean closeConnection = false;
    private Socket socket;
    private PrintWriter socketOut;
    private Scanner socketIn;
    private Scanner stdIn;
    private final SerializeDeserialize serializeDeserialize;


    public ConnectionTCP(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            stdIn = new Scanner(System.in);
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
        // todo qui inizializza la UI
        try {
            socketReader.join();
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
