package it.polimi.ingsw.client;

import it.polimi.ingsw.interfaces.InterfaceServer;

import java.rmi.RemoteException;

public class PingThread extends Thread {
    private final InterfaceServer interfaceServer;
    private final long pingInterval;
    private int failedPings;
    private final int maxPingFailed;

    public PingThread(InterfaceServer interfaceServer) {
        this.interfaceServer = interfaceServer;
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
                        //how do I stop the client saying the server is not responding?
                        //Do i need to call a method on it?
                    }
                }
                } catch (InterruptedException e) {
                // interrupted thread
            }
        }
    }
}
