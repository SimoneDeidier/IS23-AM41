package it.polimi.ingsw.server.servercontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class SocketManager implements Runnable {

    private final SerializeDeserialize serializeDeserialize;
    private final Socket socket;

    public SocketManager(Socket socket) {
        this.socket = socket;
        serializeDeserialize = new SerializeDeserialize();
    }

    @Override
    public void run() {

        System.out.println("Socket instantiated correctly - active on port " + socket.getPort() + ".");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(true) {
                String inMsg = in.nextLine();
                String outMsg = serializeDeserialize.deserialize(inMsg);
                // todo momentaneamente ho scritto questo ma non funziona
                if(Objects.equals(outMsg, "quit")) {
                    break;
                }
                out.println(outMsg);
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
}