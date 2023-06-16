package it.polimi.ingsw.server.servercontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SocketManager implements Runnable {

    private final SerializeDeserialize serializeDeserialize;
    private final Socket socket;
    private boolean closeConnection = false;
    private Scanner socketInput;
    private PrintWriter socketOutput;

    public SocketManager(Socket socket) {
        this.socket = socket;
        serializeDeserialize = new SerializeDeserialize(this);
    }

    @Override
    public void run() {
        System.out.println("Socket instantiated correctly - active on port " + socket.getPort() + ".");
        try {
            socketInput = new Scanner(socket.getInputStream());
            socketOutput = new PrintWriter(socket.getOutputStream());
            while(!closeConnection) {
                String inMsg = socketInput.nextLine();
                if(inMsg != null) {
                    serializeDeserialize.deserialize(inMsg);
                }
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public PrintWriter getSocketOutput() {
        return socketOutput;
    }

    public void closeConnection() throws IOException {
        this.closeConnection = true;
        socketInput.close();
        socketOutput.close();
        socket.close();
        System.out.println("Socket close correctly - port: " + socket.getPort() + ".");
    }

    public Socket getSocket() {
        return this.socket;
    }
}
