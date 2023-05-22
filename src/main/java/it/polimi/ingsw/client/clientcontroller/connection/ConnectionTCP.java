package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.clientcontroller.SerializeDeserialize;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ConnectionTCP implements Connection {


    private boolean closeConnection = false;
    private Socket socket;
    private PrintWriter socketOut;
    private Scanner socketIn;
    private final SerializeDeserialize serializeDeserialize;

    private String IP = null;
    private int PORT = 0;

    public ConnectionTCP(String ip, int port) {
        this.IP = ip;
        this.PORT = port;
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
                if ((inMsg = socketIn.nextLine()) != null) {
                    try {
                        serializeDeserialize.deserialize(inMsg);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    public void rejoinMatch() {
        try {
            socket = new Socket(IP, PORT);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread socketReader = new Thread(() -> {
            while(!closeConnection) {
                String inMsg;
                if ((inMsg = socketIn.nextLine()) != null) {
                    try {
                        serializeDeserialize.deserialize(inMsg);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        socketReader.start();
        // serializeDeserialize.startUserInterface(uiType);
        try {
            socketReader.join();
            System.err.println("JOINED THE SOCKET THREAD");
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

}
