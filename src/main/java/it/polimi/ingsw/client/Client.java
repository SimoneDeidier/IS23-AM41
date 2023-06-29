package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientcontroller.connection.Connection;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionRMI;
import it.polimi.ingsw.client.clientcontroller.connection.ConnectionTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;
/**
 * The Client class represents the client application for connecting to the server.
 */
public class Client {

    private static Connection connection;
    private static String ipAddress;
    private static int portNumber;
    private static String connectionType;
    private static String uiType;
    private static boolean connectionOk = true;

    /**
     * The main method for starting the client application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {

        drawLogo();
        System.out.println("\n\nWARNING!: Please if your OS is a Unix based, remember to run the command \"rmiregistry &\" on a new terminal!\n\n");
        Scanner stdin = new Scanner(System.in);
        if(!parseConnectionType(args)) {
            do {
                System.out.println("Select connection type: ");
                connectionType = stdin.nextLine();
            } while(!connectionType.equalsIgnoreCase("tcp") && !connectionType.equalsIgnoreCase("rmi"));
        }
        if(!parseIPAddress(args)) {
            do {
                System.out.println("Insert the server's IP address: ");
                ipAddress = stdin.nextLine();
            } while(!ipAddress.matches("[0-9][0-9.]*[0-9]+") && !ipAddress.equalsIgnoreCase("localhost"));
        }
        if(!parsePortNumber(args)) {
            do {
                System.out.println("Insert the server's port number: ");
                portNumber = stdin.nextInt();
            } while(portNumber <= 1024 || portNumber > 65535);
        }
        switch (connectionType.toLowerCase()) {
            case "tcp" -> {
                try {
                    connection = new ConnectionTCP(ipAddress, portNumber);
                }
                catch (IOException e) {
                    connectionOk = false;
                }
            }
            case "rmi" -> {
                try {
                    connection = new ConnectionRMI(portNumber, ipAddress);
                } catch (RemoteException e) {
                    connectionOk = false;
                }
            }
            default -> System.err.println("Wrong parameter, restart client...");
        }
        if(connectionOk) {
            if (!parseUiType(args)) {
                do {
                    System.out.println("Select UI type: ");
                    uiType = stdin.nextLine();
                } while(!uiType.equalsIgnoreCase("tui") && !uiType.equalsIgnoreCase("gui"));
            }
            switch (uiType.toLowerCase()) {
                case "tui" -> connection.startConnection("tui");
                case "gui" -> connection.startConnection("gui");
                default -> System.err.println("Wrong parameter, restart client...");
            }
        }
        else {
            System.out.println("Server is momentarily unreachable, please retry later!");
        }
        try {
            connection.closeConnection();
        }
        catch (NullPointerException e) {
            System.out.println("Connection never opened!");
        }
        System.out.println("Closing the client...");
    }
    /**
     * Draws the logo of the game.
     */
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
    /**
     * Parses the IP address from the command-line arguments.
     *
     * @param args The command-line arguments.
     * @return True if the IP address is successfully parsed, false otherwise.
     */
    public static boolean parseIPAddress(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i + 1];
            if(Objects.equals(cmd, "--ipaddr") && (par.matches("[0-9][0-9.]*[0-9]+") || par.equalsIgnoreCase("localhost"))) {
                ipAddress = par;
                return true;
            }
        }
        return false;
    }
    /**
     * Parses the connection type from the command-line arguments.
     *
     * @param args The command-line arguments.
     * @return True if the connection type is successfully parsed, false otherwise.
     */
    public static boolean parseConnectionType(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i+1];
            if((Objects.equals(cmd, "-c") || Objects.equals(cmd, "--conn")) && (par.equalsIgnoreCase("tcp") || par.equalsIgnoreCase("rmi"))) {
                connectionType = par;
                return true;
            }
        }
        return false;
    }
    /**
     * Parses the UI type from the command-line arguments.
     *
     * @param args The command-line arguments.
     * @return True if the UI type is successfully parsed, false otherwise.
     */
    public static boolean parseUiType(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i+1];
            if((Objects.equals(cmd, "-u") || Objects.equals(cmd, "--ui")) && (par.equalsIgnoreCase("tui") || par.equalsIgnoreCase("gui"))) {
                uiType = par;
                return true;
            }
        }
        return false;
    }
    /**
     * Parses the port number from the command-line arguments.
     *
     * @param args The command-line arguments.
     * @return True if the port number is successfully parsed, False otherwise.
     */
    public static boolean parsePortNumber(String[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            String cmd = args[i];
            String par = args[i+1];
            if((Objects.equals(cmd, "-p") || Objects.equals(cmd, "--port")) && (Integer.parseInt(par) > 1024 && Integer.parseInt(par) <= 65535)) {
                portNumber = Integer.parseInt(par);
                return true;
            }
        }
        return false;
    }

}
