package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.messages.Body;
import it.polimi.ingsw.messages.TCPMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static final String IP = "localhost";
    private static final int PORT = 8888;
    private static final Gson gson = new Gson();
    private static boolean closeConnection = false;
    private static Socket socket;
    private static PrintWriter socketOut;
    private static Scanner socketIn;
    private static Scanner stdIn;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            stdIn = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawLogo() {
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("files/MyShelfieLogo.txt");
            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String string;
                while((string = reader.readLine()) != null) {
                    System.out.println(string);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() {
        Thread socketReader = new Thread(() -> {
            while(!closeConnection) {
                String inMsg;
                while ((inMsg = socketIn.nextLine()) != null) {
                    TCPMessage t = gson.fromJson(inMsg, TCPMessage.class);
                    System.out.println("New msg: " + t.getBody().getText());
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

    public void closeConnection() {
        closeConnection = true;
    }


    public static void main(String[] args) {

        Client client = new Client(IP, PORT);
        client.drawLogo();
        client.startClient();

    }

}
