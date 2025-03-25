package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPException;
import io.github.totchi_lagawi.http_utils.HTTPMethod;
import io.github.totchi_lagawi.http_utils.HTTPRequest;
import io.github.totchi_lagawi.http_utils.HTTPResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Class representing a connexion to a client
 */
public class ServerConnexion {
    // The socket holding the connexion
    private Socket _socket;
    // The local address of the connexion
    private InetSocketAddress _localAddress;
    // The remote address of the connexion
    private InetSocketAddress _remoteAddress;
    // The latency of the connexion
    // 0 before any measurement, hopefully getLatency() does it before returning the
    // value
    private float _latency;
    // The current state of the connexion
    private ConnexionState _state = ConnexionState.CLOSED;
    // The hostname of the server, not required
    private Optional<String> _hostname = Optional.empty();
    // The origin that any incoming connection must satisfy, not required
    private Optional<String> _requiredOrigin = Optional.empty();
    // The location of the server
    private String _location;
    // The subprotocol as requested by the client during the handshake, not required
    private Optional<String> _subprotocol = Optional.empty();
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
        this._localAddress = new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());
        this._remoteAddress = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
        this._hostname = Optional.of("localhost");
        this._location = "/";
    }

    /**
     * Get the local address of the connexion
     * 
     * @return the local address of the connexion
     */
    public InetSocketAddress getLocalAddress() {
        return this._localAddress;
    }

    /**
     * Get the remote address of the connexion
     * 
     * @return the remote address of the connexion
     */
    public InetSocketAddress getRemoteAddress() {
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
     * Get the subprotocol of the connexion negociated during the handshake, may
     * return empty Optional if none was negiociated
     * 
     * @return the subprotocol
     */
    public Optional<String> getSubprotocol() {
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
        try {
            return this.getNextMessage(-1);
        } catch (TimeoutException e) {
            // This code can't be reached, the timeout is infinite
            return null;
        }
    }

    public byte[] getNextMessage(int timeout) throws TimeoutException {
        return null;
    }

    public WebSocketRequest<?> getNextRequest() throws HTTPException, IOException {
        return this.getNextRequest(-1);
    }

    public WebSocketRequest<?> getNextRequest(int timeout) throws HTTPException, IOException {
        HTTPRequest request = new HTTPRequest(new InputStreamReader(this._socket.getInputStream()), false, false);
        return new WebSocketRequest<HTTPRequest>(request);
    }

    /**
     * Close the connexion to the client
     */
    public void close() throws IOException {
        this._socket.close();
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
     * Perform WebSocket handshake
     * 
     * @throws HTTPException            if the WebSocket handshake request is
     *                                  invalid
     * 
     * @throws IllegalStateException    if the connexion is already opened, or if
     *                                  the
     *                                  received datas isn't an HTTP response
     * @throws IOException              if the input stream of the socket can't be
     *                                  accessed
     * 
     * @throws TimeoutException         if the handshake request didn't come after
     *                                  10
     *                                  seconds
     * 
     * @throws NoSuchAlgorithmException if the SHA-1 algorithm needed to perform a
     *                                  WebSocket handshake isn't available
     */
    public void performHandshake()
            throws HTTPException, IllegalStateException, IOException, TimeoutException, NoSuchAlgorithmException,
            SocketTimeoutException {
        if (this._state != ConnexionState.CLOSED) {
            throw new IllegalStateException("Connexion is used, can't perfom handshake");
        }

        // Prevent a client to hold a connection without doing anything
        this._socket.setSoTimeout(10000);

        HTTPRequest request = new HTTPRequest(new InputStreamReader(this._socket.getInputStream()), false, false);

        this._checkHTTPHandhakeRequestValidity(request);

        // If the WebSocket version used by the client isn't the same as the one used by
        // the server, it needs to tell the client which version to use
        if (!request.getHeaders().get("Sec-WebSocket-Version").equals("13")) {
            HashMap<String, String> responseHeaders = new HashMap<>();
            responseHeaders.put("Sec-WebSocket-Version", "13");
            HTTPResponse response = new HTTPResponse(request.getVersion(), 400, responseHeaders, null);
            this.sendHTTPResponse(response);

            // Handle the second handshake request
            request = new HTTPRequest(new InputStreamReader(this._socket.getInputStream()), false, false);
            this._checkHTTPHandhakeRequestValidity(request);

            // If the client refused to use the sent WebSocket version, fail handshake
            if (!request.getHeaders().get("Sec-WebSocket-Version").equals("13")) {
                throw new HTTPException(400, "The client refused to use WebSocket version 13");
            }
        }

        // Take note of the subprotocol, if specified
        if (request.getHeaders().containsKey("Sec-WebSocket-Protocol")) {
            this._subprotocol = Optional.of(request.getHeaders().get("Sec-WebSocket-Protocol"));
        }

        HashMap<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Connection", "Upgrade");
        responseHeaders.put("Upgrade", "websocket");
        responseHeaders.put("Sec-WebSocket-Accept", Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-1").digest(
                        (request.getHeaders().get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                .getBytes())));
        if (this._subprotocol.isPresent()) {
            responseHeaders.put("Sec-WebSocket-Protocol", this._subprotocol.get());
        }
        HTTPResponse response = new HTTPResponse(request.getVersion(), 101, responseHeaders, null);
        this.sendHTTPResponse(response);

        // Reset the socket timeout
        this._socket.setSoTimeout(0);

        this._state = ConnexionState.OPEN;
    }

    private void _checkHTTPHandhakeRequestValidity(HTTPRequest request) throws HTTPException {
        // Check the location
        if (!request.getLocation().equals(this._location)) {
            throw new HTTPException(404);
        }

        // Check the HTTP version
        if (request.getVersionDouble() < 1.1) {
            throw new HTTPException(400, "The request should use at least HTTP version 1.1");
        }

        // Check the HTTP method
        if (request.getMethod() != HTTPMethod.GET) {
            throw new HTTPException(400, "The request should use the GET method");
        }

        // Check the Host header
        if (!request.getHeaders().containsKey("Host")
                || ((this._hostname.isPresent() && request.getHeaders().get("Host").equals(this._hostname.get())))
                || request.getHeaders().get("Host").equals(this._localAddress.toString())) {
            throw new HTTPException(400, "Got the wrong hostname in the WebSocket handshake");
        }

        // Check the Origin header, if needed
        if (this._requiredOrigin.isPresent() && request.getHeaders().containsKey("Origin")
                && request.getHeaders().get("Origin").equals(this._requiredOrigin.get())) {
            throw new HTTPException(403, "The request \"Origin\" header didn't match the required value");
        }

        // Check the Connection header
        if (!request.getHeaders().containsKey("Connection")
                || !request.getHeaders().get("Connection").toLowerCase(Locale.ROOT).contains("upgrade")) {
            throw new HTTPException(400, "The request should contain the \"Connection: Upgrade\" header field");
        }

        // Check the key size
        // Each char can represent up to 64 values, so 6 bits (log2(64) = 6, 2^6 = 64)
        // The closest multiple of 8 is 24 : 6 * 4 = 24
        // Thus, the number of chars to represent n bytes is equal to ceil(4(n/3)),
        // which is in our case equal to 24
        if (!request.getHeaders().containsKey("Sec-WebSocket-Key")
                || request.getHeaders().get("Sec-WebSocket-Key").length() != 24) {
            throw new HTTPException(400,
                    "The request should contain a \"Sec-WebSocket-Version\" header with 16 chars encoded in base64");
        }

        // Check the presence of the WebSocket version header
        if (!request.getHeaders().containsKey("Sec-WebSocket-Version")) {
            throw new HTTPException(400, "The request should contain the \"Sec-WebSocket-Version\" header");
        }
    }
}