package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientcontroller.connection.Connection;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionTCP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private static Connection connection;
    private final static String IP = "localhost";
    private final static int TCP_PORT = 8888;
    private final static int RMI_PORT = 1234;
    private static String connectionType;
    private static String uiType;

    public static void main(String[] args) {

        drawLogo();
        Scanner stdin = new Scanner(System.in);
        if(!parseConnectionType(args)) {
            System.out.println("Select connection type: ");
            connectionType = stdin.nextLine();
        }
        switch (connectionType) {
            case "tcp" -> connection = new ConnectionTCP(IP, TCP_PORT);
            case "rmi" -> connection = new ConnectionRMI(IP, RMI_PORT);
            default -> System.err.println("Wrong parameter, restart client...");
        }
        if(!parseUiType(args)) {
            System.out.println("Select UI type: ");
            uiType = stdin.nextLine();
        }
        switch (uiType) {
            case "tui" -> connection.startConnection("tui");
            case "gui" -> connection.startConnection("gui");
            default -> System.err.println("Wrong parameter, restart client...");
        }

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

    public static boolean parseConnectionType(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i+1];
            if(Objects.equals(cmd, "-c") && (Objects.equals(par, "tcp") || Objects.equals(par, "rmi"))) {
                connectionType = par;
                return true;
            }
        }
        return false;
    }

    public static boolean parseUiType(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i+1];
            if(Objects.equals(cmd, "-u") && (Objects.equals(par, "tui") || Objects.equals(par, "gui"))) {
                uiType = par;
                return true;
            }
        }
        return false;
    }

}
