package it.polimi.ingsw.client;

import it.polimi.ingsw.interfaces.InterfaceClient;
import it.polimi.ingsw.interfaces.InterfaceServer;

import java.rmi.RemoteException;

public class PingThreadClientRmiToServer extends Thread {
    private final InterfaceServer interfaceServer;
    private InterfaceClient interfaceClient; //The client who's pinging the server
    private final long pingInterval;
    private int failedPings;
    private final int maxPingFailed;

    public PingThreadClientRmiToServer(InterfaceServer interfaceServer, InterfaceClient interfaceClient) {
        this.interfaceServer = interfaceServer;
        this.interfaceClient=interfaceClient;
        this.pingInterval = 1000;
        this.maxPingFailed=5;
        this.failedPings=0;
    }

    public void run() {
        while (true) {
            try {
                try {
                    interfaceServer.clearRMI();
                    failedPings=0;
                    Thread.sleep(pingInterval);
                } catch (RemoteException e) {
                    failedPings++;
                    if(failedPings > maxPingFailed){
                        interfaceClient.disconnectUser(8);
                    }
                }
                } catch (InterruptedException | RemoteException e) {
                // interrupted thread or the client is dead
            }
        }
    }
}
