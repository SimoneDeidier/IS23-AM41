package it.polimi.ingsw.server;

import it.polimi.ingsw.server.servercontroller.SocketManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static int port;

    public static boolean parsePortNumber(String[] args) {

        for(int i = 0; i < args.length - 1; i++) {
            if(Objects.equals(args[i], "-p") && args[i+1].matches("[0-9]+")) {
                int parsed = Integer.parseInt(args[i+1]);
                if(parsed <= 1024 || parsed > 65535) {
                    System.out.println("Invalid port number.");
                    return false;
                }
                port = parsed;
                return true;
            }
        }
        return false;

    }

    public static int setPortNumber() {

        int input;
        Scanner in = new Scanner(System.in);

        do {
            System.out.print("Please select a port number: ");
            input = in.nextInt();
        }while(input <= 1024 || input > 65535);
        return input;

    }

    public static void main(String[] args) {

        if(!parsePortNumber(args)) {
            port = setPortNumber();
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready - listening on port " + port + ".");
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketManager(socket));
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
        System.out.println("Server socket was closed - server shutting down.");
        executor.shutdown();
        System.out.println("All threads are joined - server shuttind down.");

    }

}
