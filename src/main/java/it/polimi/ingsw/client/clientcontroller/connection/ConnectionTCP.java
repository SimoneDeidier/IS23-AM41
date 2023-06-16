package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.clientcontroller.SerializeDeserialize;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionTCP implements Connection {


    private boolean closeConnection = false;
    private Socket socket;
    private PrintWriter socketOut;
    private Scanner socketIn;
    private SerializeDeserialize serializeDeserialize = null;

    private String IP = null;
    private int PORT = 0;

    public ConnectionTCP(String ip, int port) throws IOException {
        this.IP = ip;
        this.PORT = port;
        System.out.println("PRE SOCKET");
        this.socket = new Socket(ip, port);
        System.out.println("POST SOCKET");
        this.socketIn = new Scanner(socket.getInputStream());
        this.socketOut = new PrintWriter(socket.getOutputStream(), true);
        this.serializeDeserialize = new SerializeDeserialize(this);
    }

    @Override
    public void startConnection(String uiType) {
        Thread socketReader = new Thread(() -> {
            while (!closeConnection) {
                String inMsg = null;
                try {
                    inMsg = socketIn.nextLine();
                } catch (NoSuchElementException | IllegalStateException e) {
                    closeConnection = true;
                }
                if (inMsg != null) {
                    try {
                        serializeDeserialize.deserialize(inMsg);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("I'M OUT THE TCP THREAD");
        });
        socketReader.start();
        serializeDeserialize.startUserInterface(uiType);
        try {
            socketReader.join();
            System.err.println("JOINED THE SOCKET THREAD");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getSocketOut() {
        return socketOut;
    }

    @Override
    public void closeConnection() {
        this.closeConnection = true;
        socketOut.close();
        socketIn.close();
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
