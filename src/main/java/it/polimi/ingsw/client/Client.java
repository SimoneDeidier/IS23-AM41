package it.polimi.ingsw.client;

import com.google.gson.Gson;
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
            Socket socket = new Socket(IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            Scanner cli = new Scanner(System.in);
            while(!closeConnection) {
                System.out.println("Insert header: ");
                String header = cli.nextLine();
                TCPMessage tcpMsg = new TCPMessage(header, null);
                String outMsg = gson.toJson(tcpMsg);
                out.println(outMsg);
                out.flush();
                String inMsg = in.nextLine();
                TCPMessage inTCPMsg = gson.fromJson(inMsg, TCPMessage.class);
                System.out.println("New response: " + inTCPMsg.getHeader());
                if(Objects.equals(inTCPMsg.getHeader(), "Goodbye")) {
                    closeConnection = true;
                }
            }
            out.close();
            in.close();
            socket.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
