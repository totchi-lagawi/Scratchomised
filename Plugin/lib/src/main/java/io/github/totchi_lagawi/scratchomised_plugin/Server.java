package io.github.totchi_lagawi.scratchomised_plugin;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;

import io.github.totchi_lagawi.scratchomised_plugin.utils.websocket.WebSocketServer;

// Class representing the server
public class Server extends WebSocketServer {

    public Server(int port) throws BindException, IOException {
        super(port);
    }

    protected void onStart(Socket connexion) {

    }

    protected void onStop(Socket connexion) {

    }

    protected void onError(Socket connexion) {

    }

    protected void onMessage(Socket connexion, String message) {

    }

}
