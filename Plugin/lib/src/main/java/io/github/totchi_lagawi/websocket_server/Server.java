package io.github.totchi_lagawi.websocket_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract implementation of a WebSocket server
 */
public abstract class Server implements Runnable {
    // The port on which to run the server
    private int _port;
    // The server socket
    private ServerSocket _socket;

    /**
     * Contructor of the Server class
     * 
     * @param port the port to run the server on
     */
    public Server(int port) throws IOException {
        this._port = port;
    }

    /**
     * Called when a connexion just opened
     * 
     * @param connexion the connexion which opened
     */
    abstract protected void onOpen(ServerConnexion connexion);

    /**
     * Called when a connexion receives a text message
     * 
     * @param connexion the connexion which received the message
     * @param message   the message received
     */
    abstract protected void onMessage(ServerConnexion connexion, String message);

    /**
     * Called when a connexion receives a binary message
     * 
     * @param connexion the connexion which received the message
     * @param message   the message received
     */
    abstract protected void onMessage(ServerConnexion connexion, byte[] message);

    /**
     * Called when a connexion closes
     * 
     * @param connexion the connexions which just closed
     */
    // TODO maybe add the close code and reason?
    abstract protected void onClose(ServerConnexion connexion);

    /**
     * Called when a connexion encounters an error
     * 
     * @param connexion the connexion which encountered the error
     * @param exception the exception the connexion encountered
     */
    abstract protected void onError(ServerConnexion connexion, Exception exception);

    /**
     * The main server loop
     */
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            this._socket = new ServerSocket(this._port);
            while (true) {
                executor.submit(new ConnexionHandler(this._socket.accept(), this));
            }
        } catch (SocketException ex) {
            // SocketException means the socket was closed, by this.stop()
            // We should just return
            executor.shutdown();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stop the server
     */
    public void stop() {
        try {
            while (!this._socket.isClosed()) {
                this._socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}