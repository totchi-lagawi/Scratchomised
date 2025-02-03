package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPStatusCode;

import java.net.Socket;
import java.net.SocketAddress;

/**
 * Class representing a connexion to a client
 */
public class ServerConnexion {
    // The socket holding the connexion
    private Socket _socket;
    // The local address of the connexion
    private SocketAddress _localAddress;
    // The remote address of the connexion
    private SocketAddress _remoteAddress;
    // The latency of the connexion
    // 0 before any measurement, hopefully getLatency() does it before returning the
    // value
    private float _latency;
    // The current state of the connexion
    private ConnexionState _state;
    // The subprotocol as requested by the client during the handshake
    private String _subprotocol;
    // The close code
    private int _close_code;
    // The close reason
    private String _close_reason;

    /**
     * Get the local address of the connexion
     * 
     * @return the local address of the connexion
     */
    public SocketAddress getLocalAddress() {
        return this._localAddress;
    }

    /**
     * Get the remote address of the connexion
     * 
     * @return the remote address of the connexion
     */
    public SocketAddress getRemoteAddress() {
        return this._remoteAddress;
    }

    /**
     * Get the connexion state of the connexion
     * 
     * @return the state of the connexion
     */
    public ConnexionState getconnexionState() {
        return this._state;
    }

    /**
     * Get the subprotocol of the connexion, negociated during the handshake
     * 
     * @return the subprotocol
     */
    public String getSubprotocol() {
        return this._subprotocol;
    }

    /**
     * Get the close code of the connexion
     * 
     * @return the close code
     */
    public int getCloseCode() {
        return this._close_code;
    }

    /**
     * Get the close reason of the connexion
     * 
     * @return the close reason of the connexion
     */
    public String getCloseReason() {
        return this._close_reason;
    }

    /**
     * Instanciate <code>Serverconnexion</code>
     * 
     * @param socket the socket to the client
     */
    public ServerConnexion(Socket socket) {

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
     * Close the connexion to the client
     */
    public void close() {

    }

    /**
     * Wait for the connexion to the client to be closed
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
     * Calculate the latency, then return the calculated latency
     * 
     * @return the calculated latency
     */
    public float getLatency() {
        return this.getLatency(0);
    }

    /**
     * Calculate the latency, aborting on the given timeout
     * 
     * @param timeout the maximum time to wait for the pong of the client
     * 
     * @return the calculated latency
     */
    public float getLatency(int timeout) {
        return -1;
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