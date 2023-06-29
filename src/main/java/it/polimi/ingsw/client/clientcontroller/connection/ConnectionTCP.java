package it.polimi.ingsw.client.clientcontroller.connection;

import it.polimi.ingsw.client.clientcontroller.SerializeDeserialize;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * The ConnectionTCP class manages the connection of TCP clients to the server.
 */
public class ConnectionTCP implements Connection {


    private boolean closeConnection = false;
    private Socket socket;
    private PrintWriter socketOut;
    private Scanner socketIn;
    private SerializeDeserialize serializeDeserialize = null;

    private String IP = null;
    private int PORT = 0;

    /**
     * Constructs a new ConnectionTCP instance with the specified parameters.
     *
     * @param ip The IP address for TCP connection.
     * @param port The port number for TCP connection.
     * @throws IOException if an I/O exception occurs.
     */
    public ConnectionTCP(String ip, int port) throws IOException {
        this.IP = ip;
        this.PORT = port;
        this.socket = new Socket(ip, port);
        this.socketIn = new Scanner(socket.getInputStream());
        this.socketOut = new PrintWriter(socket.getOutputStream(), true);
        this.serializeDeserialize = new SerializeDeserialize(this);
    }
    /**
     * Starts the TCP connection and initializes the socket reader and user interface threads.
     *
     * @param uiType the type of user interface to start (GUI o CLI)
     */
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
                        System.err.println("An unknown exception was thrown. If the problem persists, please restart the client!");
                    }
                }
            }
            socketOut.close();
            socketIn.close();
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("The socket could not be closed, please kill the task and restart the client!");
            }
        });
        socketReader.start();
        serializeDeserialize.startUserInterface(uiType);
        try {
            socketReader.join();
            System.err.println("JOINED THE SOCKET THREAD");
        }
        catch (InterruptedException e) {
            System.err.println("The socket thread could not be terminated, please kill the task and restart the client!");
        }
    }
    /**
     * Returns the PrintWriter for the socket output stream.
     *
     * @return The PrintWriter for the socket output stream.
     */
    public PrintWriter getSocketOut() {
        return socketOut;
    }
    
    /**
     * Closes the TCP connection.
     */
    @Override
    public void closeConnection() {
        this.closeConnection = true;
    }

}
