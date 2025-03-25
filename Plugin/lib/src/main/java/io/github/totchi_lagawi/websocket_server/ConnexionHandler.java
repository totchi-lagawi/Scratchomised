package io.github.totchi_lagawi.websocket_server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import io.github.totchi_lagawi.http_utils.HTTPException;
import io.github.totchi_lagawi.http_utils.HTTPResponse;
import io.github.totchi_lagawi.http_utils.HTTPVersion;

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
     * 
     * @throws HTTPException
     */
    public void run() {
        try {
            this._connexion.performHandshake();
        } catch (HTTPException ex) {
            HashMap<String, String> response_headers = new HashMap<>();
            response_headers.put("Connection", "Close");
            try {
                this._connexion
                        .sendHTTPResponse(
                                new HTTPResponse(HTTPVersion.ONE_POINT_ONE, ex.getStatusCode(), response_headers,
                                        "<h1>Error " + String.valueOf(ex.getStatusCode() + "</h1>")));
                this._connexion.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
            this._server.onError(_connexion, ex);
        } catch (Exception ex) {
            try {
                this._connexion.close();
            } catch (IOException ex2) {
                ex.printStackTrace();
            }
            this._server.onError(_connexion, ex);
        }
    }

    /**
     * Stop the connexion
     */
    public void close() throws IOException {
        this._connexion.close();
    }
}
