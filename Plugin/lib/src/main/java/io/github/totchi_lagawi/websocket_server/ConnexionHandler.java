package io.github.totchi_lagawi.websocket_server;

import java.net.Socket;

public class ConnexionHandler implements Runnable {
    // The parent server, to call methods like onOpen() or onClose()
    private Server _server;
    // The connexion to the client
    private ServerConnexion _connexion;

    /**
     * Instanciate the connexion handler
     * 
     * @param socket the socket to the client
     */
    public ConnexionHandler(Socket socket, Server server) {
        this._connexion = new ServerConnexion(socket);
        this._server = server;
    }

    /**
     * Run the connexion handler
     */
    public void run() {
        this._server.onOpen(_connexion);
        // TODO
        // Implement hanshaking, then receiving messages and passing them to the
        // appropriate methods
    }

    /**
     * Stop the connexion
     */
    public void stop() {
        this._connexion.close();
    }
}
