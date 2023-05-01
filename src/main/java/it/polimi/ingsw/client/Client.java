package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.server.servercontroller.Move;
import it.polimi.ingsw.server.servercontroller.TCPMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static final String IP = "localhost";
    private static final int PORT = 8888;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            List<int[]> list = new ArrayList<>(3);
            for(int i = 0; i < 3; i++) {
                int[] val = new int[2];
                val[0] = 1;
                val[1] = 2;
                list.add(i, val);
            }
            Move testMove = new Move();
            testMove.setPlayerNickname("Niklodeon");
            testMove.setPositionsPicked(list);
            testMove.setColumn(0);
            TCPMessage tcpMsg = new TCPMessage("Move", testMove);
            tcpMsg.printTCPMessage();
            String msg = gson.toJson(tcpMsg);
            out.println(msg);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
