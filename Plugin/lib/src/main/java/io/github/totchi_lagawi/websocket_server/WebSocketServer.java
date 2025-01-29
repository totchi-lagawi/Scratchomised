package io.github.totchi_lagawi.websocket_server;

/**
 * Abstract implementation of a WebSocket server
 */
public abstract class WebSocketServer implements Runnable {
    /**
     * Called when a connection just opened
     * 
     * @param connection the connection which opened
     */
    abstract void onOpen(ServerConnection connection);

    /**
     * Called when a connection receives a text message
     * 
     * @param connection the connection which received the message
     * @param message    the message received
     */
    abstract void onMessage(ServerConnection connection, String message);

    /**
     * Called when a connection receives a binary message
     * 
     * @param connection the connection which received the message
     * @param message    the message received
     */
    abstract void onMessage(ServerConnection connection, byte[] message);

    /**
     * Called when a connection closes
     * 
     * @param connection the connections which just closed
     */
    // TODO maybe add the close code and reason?
    abstract void onClose(ServerConnection connection);

    /**
     * Called when a connection encounters an error
     * 
     * @param connection the connection which encountered the error
     * @param exception  the exception the connection encountered
     */
    abstract void onError(ServerConnection connection, Exception exception);

    /**
     * The main server loop
     */
    public void run() {

    }
}