package io.github.totchi_lagawi.scratchomised_plugin.utils.websocket;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

import io.github.totchi_lagawi.scratchomised_plugin.LanguageManager;

public abstract class WebSocketServer implements Runnable {
    /** The socket on which the server runs */
    private ServerSocket _socket;
    /** Holds all of the current connexion threads */
    private ThreadPoolExecutor _connections;

    /**
     * Instanciate the server
     * This class should be extended
     * 
     * @param port the port on which to run the server
     * 
     * @throws BindException when it is impossible to bind the socket
     * @throws IOException
     */
    protected WebSocketServer(int port) throws BindException, IOException {
        // A server socket is assign as soon as the server class is instanciated
        // BindException is thrown if the socket can't be binded
        // Like, for example, if the specified port is inferior to 1024, but the user
        // isn't Administrator/root
        this._socket = new ServerSocket(port);
    }

    /**
     * Run the server
     */
    public void run() {
        this._connections = new ThreadPoolExecutor(2, 5, 0, null, null);
        while (true) {
            try {
                System.err.println("Waiting for request");
                // Accept client connection
                Socket client = this._socket.accept();

                System.out.println(LanguageManager.getString("log_prefix", null)
                        + "Client "
                        + client.getRemoteSocketAddress()
                        + " connected.");

                this._connections.execute(new WebSocketServerConnexionHandler(client, this));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Stop the server
     * Behing the scene, this just close the socket, thus cancelling any operating
     * on it
     */
    public void stop() {
        // While the socket isn't closed
        while (!this._socket.isClosed()) {
            // We try to close it
            try {
                this._socket.close();
            } catch (IOException ex) {
                // Socket is busy, so we'll try again
            }
        }
    }

    /**
     * Check if the server is running
     * 
     * @return <code>true</code if yes, otherwise <code>false</code>
     */
    public boolean isRunning() {
        // Prevent NullPointerException
        // Thanks null
        if (this._socket == null) {
            return false;
        }

        if (this._socket.isClosed()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Called when a connexion starts
     * 
     * @param connexion the connexion that just started
     */
    protected abstract void onStart(Socket connexion);

    /**
     * Called when a connexion ends
     * 
     * @param connexion the connexion that just ended
     */
    protected abstract void onStop(Socket connexion);

    /**
     * Called when a connexion encouters an error
     * 
     * @param connexion the connexion from which the error come from
     */
    protected abstract void onError(Socket connexion);

    /**
     * Called when a message is received
     * 
     * @param connexion the connexion from which the message come from
     * @param message   the message, as a <code>String</code>
     */
    protected abstract void onMessage(Socket connexion, String message);

}