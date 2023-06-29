package it.polimi.ingsw.server.servercontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
/**
 * The SocketManager class manages the socket connection, including reading and writing data from/to the socket.
 */
public class SocketManager implements Runnable {

    private final SerializeDeserialize serializeDeserialize;
    private final Socket socket;
    private boolean closeConnection = false;
    private Scanner socketInput;
    private PrintWriter socketOutput;
    /**
     * Constructs a new SocketManager instance with the specified socket.
     *
     * @param socket The Socket instance representing the socket connection.
     */
    public SocketManager(Socket socket) {
        this.socket = socket;
        serializeDeserialize = new SerializeDeserialize(this);
    }
    /**
     * Runs the socket manager, reading input from the socket and passing it to the SerializeDeserialize instance for processing.
     * Closes the connection when requested.
     */
    @Override
    public void run() {
        System.out.println("Socket instantiated correctly - active on port " + socket.getPort() + ".");
        try {
            socketInput = new Scanner(socket.getInputStream());
            socketOutput = new PrintWriter(socket.getOutputStream());
            while(!closeConnection) {
                String inMsg = null;
                try {
                    inMsg = socketInput.nextLine();
                }
                catch (NoSuchElementException | IllegalStateException e) {
                    closeConnection();
                }
                if(inMsg != null) {
                    serializeDeserialize.deserialize(inMsg);
                }
            }
            socketInput.close();
            socketOutput.close();
            socket.close();
            System.out.println("Socket close correctly - port: " + socket.getPort() + ".");
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /**
     * Returns the PrintWriter for writing output to the socket.
     *
     * @return The PrintWriter instance.
     */
    public PrintWriter getSocketOutput() {
        return socketOutput;
    }
    /**
     * Closes the socket connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void closeConnection() throws IOException {
        this.closeConnection = true;
    }
    /**
     * Returns the Socket instance representing the socket connection.
     *
     * @return The Socket instance.
     */
    public Socket getSocket() {
        return this.socket;
    }
}
