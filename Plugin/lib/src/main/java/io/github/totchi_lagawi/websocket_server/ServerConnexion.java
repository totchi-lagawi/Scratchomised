package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPException;
import io.github.totchi_lagawi.http_utils.HTTPRequest;
import io.github.totchi_lagawi.http_utils.HTTPResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private ConnexionState _state = ConnexionState.CLOSED;
    // The subprotocol as requested by the client during the handshake
    private String _subprotocol;
    // The close code
    private int _close_code;
    // The close reason
    private String _close_reason;

    /**
     * Instanciate <code>Serverconnexion</code>
     * 
     * @param socket the socket to the client
     */
    public ServerConnexion(Socket socket) {
        this._socket = socket;
        this._localAddress = socket.getLocalSocketAddress();
        this._remoteAddress = socket.getRemoteSocketAddress();
    }

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
    public ConnexionState getConnexionState() {
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
     * Send a frame to the client
     * 
     * @param frame the frame to be sent
     */
    public void send(Frame frame) {

    }

    public byte[] getNextMessage() {
        return this.getNextMessage(-1);
    }

    public byte[] getNextMessage(int timeout) {
        return null;
    }

    public WebSocketResponse<?> getNextRequest() {
        return this.getNextRequest(-1);
    }

    public WebSocketResponse<?> getNextRequest(int timeout) {
        return null;
    }

    /**
     * Close the connexion to the client
     */
    public void close() {

    }

    /**
     * Send a ping request to the client
     */
    public void ping() {

    }

    /**
     * Send a ping response to the client
     */
    public void pong() {

    }

    /**
     * Calculate the latency, then return the calculated latency
     * 
     * @return the calculated latency
     */
    public float getLatency() {
        return this.getLatency(-1);
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
     * @param response the response to send
     */
    public void sendHTTPResponse(HTTPResponse response) throws IOException {
        this._socket.getOutputStream().write(response.getRawResponse().getBytes());
    }

    /**
     * Perform Websocket handshake
     * 
     * @throws IllegalStateException if the connexion is already opened, or if the
     *                               received datas isn't an HTTP response
     * @throws IOException           if the input stream of the socket can't be
     *                               accessed
     */
    @SuppressWarnings("unchecked")
    public void performHandshake() throws HTTPException, IllegalStateException, IOException {
        if (this._state != ConnexionState.CLOSED) {
            throw new IllegalStateException("Connexion is used, can't perfom handshake");
        }

        WebSocketResponse<HTTPResponse> response = null;

        try {
            response = (WebSocketResponse<HTTPResponse>) this.getNextRequest(10);
            if (response.type != WebSocketResponseType.HTTP_RESPONSE) {
                // Weird, I don't even know how this would be possible
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Got a non-HTTP request while performing handshake");
        }

        InputStream reader = this._socket.getInputStream();

        HTTPRequest request = new HTTPRequest(new InputStreamReader(reader), false, false);

    }
}