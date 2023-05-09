package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.server.servercontroller.Body;
import it.polimi.ingsw.server.servercontroller.SerializeDeserialize;
import it.polimi.ingsw.server.servercontroller.TCPMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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

    public static void main(String[] args) {

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
        try {
            socket = new Socket(IP, PORT);
            socketOut = new PrintWriter(socket.getOutputStream());
            socketIn = new Scanner(socket.getInputStream());
            stdIn = new Scanner(System.in);

            // threads

            Thread socketReader = new Thread(() -> {
                while(true) {
                    String inMsg;
                    while ((inMsg = socketIn.nextLine()) != null) {
                        TCPMessage t = gson.fromJson(inMsg, TCPMessage.class);
                        System.out.println("New msg: " + t.getBody().getText());
                    }
                }
            });

            Thread socketWriter = new Thread(() -> {
                while(true) {
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
            socket.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
