package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static Connection connection;

    private final static String IP = "localhost";
    private final static int TCP_PORT = 8888;
    private final static int RMI_PORT = 1234;

    public static void main(String[] args) {

        drawLogo();
        Scanner stdin = new Scanner(System.in);
        System.out.println("Select connection type: ");
        String type = stdin.nextLine();
        switch (type) {
            case "tcp" -> {
                connection = new ConnectionTCP(IP, TCP_PORT);
            }
            case "rmi" -> {
                connection = new ConnectionRMI(IP, RMI_PORT);
            }
            default -> {
                System.err.println("Wrong parameter, restart server...");
            }
        }
        connection.startConnection();

    }

    public static void drawLogo() {
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

}
