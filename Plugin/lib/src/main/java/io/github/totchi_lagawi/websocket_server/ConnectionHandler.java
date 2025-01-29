package io.github.totchi_lagawi.websocket_server;

import java.net.Socket;

public class ConnectionHandler implements Runnable {
    // The parent server, to call methods like onOpen() or onClose()
    private Server _server;
    // The connection to the client
    private ServerConnection _connection;

    /**
     * Instanciate the connection handler
     * 
     * @param socket the socket to the client
     */
    public ConnectionHandler(Socket socket) {
        this._connection = new ServerConnection(socket);
    }

    /**
     * Run the connection handler
     */
    public void run() {
        // TODO
        // Implement hanshaking, then receiving messages and passing them to the
        // appropriate methods
    }

    /**
     * Stop the connection
     */
    public void stop() {
        this._connection.close();
    }
}
