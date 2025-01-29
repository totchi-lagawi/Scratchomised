package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPStatusCode;

import java.net.Socket;
import java.net.SocketAddress;

/**
 * Class representing a connection to a client
 */
public class ServerConnection {
    // The socket holding the connection
    private Socket _socket;
    // The local address of the connection
    private SocketAddress _localAddress;
    // The remote address of the connection
    private SocketAddress _remoteAddress;
    // The latency of the connection
    // 0 before any measurement, hopefully getLatency() does it before returning the
    // value
    private float _latency;
    // The current state of the connection
    private ConnectionState _state;
    // The subprotocol as requested by the client during the handshake
    private String _subprotocol;
    // The close code
    private int _close_code;
    // The close reason
    private String _close_reason;

    /**
     * Instanciate <code>ServerConnection</code>
     * 
     * @param socket the socket to the client
     */
    public ServerConnection(Socket socket) {

    }

    /**
     * Wait for the next message, and return it
     * 
     * @return the message received
     */
    public byte[] recv() {
        return null;
    }

    /**
     * Send text message to the client
     * 
     * @param message the text to be sent
     */
    public void send(String message) {

    }

    /**
     * Send bytes message to the client
     * 
     * @param message the bytes to be sent
     */
    public void send(byte[] message) {

    }

    /**
     * Close the connection to the client
     */
    public void close() {

    }

    /**
     * Wait for the connection to the client to be closed
     */
    public void wait_closed() {

    }

    /**
     * Send a ping request to the client
     */
    public void ping() {

    }

    /**
     * Send a ping response to the client, useful in an unidirectional stream as a
     * keepalive
     */
    public void pong() {

    }

    /**
     * Send an HTTP response to the client
     * 
     * @param status the status code of the response
     * @param body   the body of the response
     */
    public void respond(HTTPStatusCode status, String body) {

    }
}