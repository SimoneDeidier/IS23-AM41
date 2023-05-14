package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;
import it.polimi.ingsw.client.clientontroller.SerializeDeserialize;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
    public void startConnection() {
        Thread socketReader = new Thread(() -> {
            while(!closeConnection) {
                String inMsg;
                while ((inMsg = socketIn.nextLine()) != null) {
                    serializeDeserialize.deserialize(inMsg);
                }
            }
        });
        Thread socketWriter = new Thread(() -> {
            while(!closeConnection) {
                String newMsg;
                System.out.println("Insert new message: ");
                newMsg = stdIn.nextLine();
                Body b = new Body();
                b.setText(newMsg);
                TCPMessage t = new TCPMessage("Broadcast Message", b);
                String s = gson.toJson(t);
                socketOut.println(s);
                socketOut.flush();
            }
        });
        socketWriter.start();
        socketReader.start();
        try {
            socketWriter.join();
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

    public void setCloseConnection(boolean closeConnection) {
        this.closeConnection = closeConnection;
    }

}
