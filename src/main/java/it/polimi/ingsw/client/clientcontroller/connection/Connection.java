package it.polimi.ingsw.client.clientcontroller.connection;

public interface Connection {

    /**
     * Starts the connection.
     *
     * @param uiType the type of user interface to start (GUI o CLI)
     */
    void startConnection(String uiType);

    /**
     * Closes the connection.
     */
    void closeConnection();
}
