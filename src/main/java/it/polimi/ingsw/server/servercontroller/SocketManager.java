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

    public SocketManager(Socket socket) {
        this.socket = socket;
        serializeDeserialize = new SerializeDeserialize(this);
    }

    @Override
    public void run() {
        System.out.println("Socket instantiated correctly - active on port " + socket.getPort() + ".");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(!closeConnection) {
                System.out.println("ENTRATO NEL WHILE");
                String inMsg = in.nextLine();
                System.out.println("TCP INPUT STRING: " + inMsg);
                List<String> outMessages = serializeDeserialize.deserialize(inMsg);
                for(String s : outMessages) {
                    out.println(s);
                }
                out.flush();
            }
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void closeConnection() {
        this.closeConnection = true;
    }
}
