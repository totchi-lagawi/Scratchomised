package io.github.totchi_lagawi.websocket_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.github.totchi_lagawi.http_utils.HTTPException;
import io.github.totchi_lagawi.http_utils.HTTPMethod;
import io.github.totchi_lagawi.http_utils.HTTPRequest;
import io.github.totchi_lagawi.http_utils.HTTPResponse;
import io.github.totchi_lagawi.http_utils.HTTPVersion;
import io.github.totchi_lagawi.scratchomised_plugin.LanguageManager;

/**
 * A WebSocket server, compliant (STILL WORK IN PROGRESS) with
 * https://datatracker.ietf.org/doc/html/rfc6455.html
 */
public abstract class WebSocketServer implements Runnable {
    private ExecutorService _service;
    /** The socket on which the server runs */
    private ServerSocket _socket;

    /**
     * Instanciate the server
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
        // Won't a FixedThreadPool limit the number of connexions?
        this._service = Executors.newFixedThreadPool(5);
        while (true) {
            try {
                // Accept client connection
                Socket client = this._socket.accept();

                System.out.println(LanguageManager.getString("log_prefix", null)
                        + "Client "
                        + client.getRemoteSocketAddress()
                        + " connected.");

                this._service.submit(new WebSocketServerConnexionHandler(client, this));
            } catch (SocketException ex) {
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }

    }

    /**
     * Stop the server.
     * Behing the scene, this just close the socket, thus cancelling any operating
     * on it.
     */
    public void stop() {
        this._service.shutdown();
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
     * Check if the server is running.
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
     * Called when a connexion starts.
     * 
     * @param connexion the connexion that just started
     */
    protected abstract void onStart(Socket connexion);

    /**
     * Called when a connexion ends.
     * 
     * @param connexion the connexion that just ended
     */
    protected abstract void onStop(Socket connexion);

    /**
     * Called when a connexion encouters an error.
     * 
     * @param connexion the connexion from which the error come from
     */
    protected abstract void onError(Socket connexion);

    /**
     * Called when a message is received.
     * 
     * @param connexion the connexion from which the message come from
     * @param message   the message, as a <code>String</code>
     */
    protected abstract void onMessage(Socket connexion, String message);

    /**
     * The class managing current WebSocket connections.
     */
    private class WebSocketServerConnexionHandler implements Runnable {
        // The socket to the client
        private Socket _connexion;
        // The server class
        // Used when calling "onMessage" and other functions
        private WebSocketServer _server;
        // Used to communicate with the client
        private HTTPVersion _HTTPVersion;

        /**
         * Instanciate the connexion handler
         * 
         * @param connexion
         * @param server
         */
        public WebSocketServerConnexionHandler(Socket connexion, WebSocketServer server) {
            this._connexion = connexion;
            this._server = server;
        }

        /**
         * Run the connexion handler.
         */
        public void run() {
            // Perfom the handshake
            try {
                this._handshake();

                while (true) {
                    // TODO
                }
            } catch (WebSocketException ex) {
                System.err.println(LanguageManager.getString("log.prefix", null) + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }

        /**
         * Perfom WebSocket handshake.
         * 
         * @throws IllegalArgumentException if the request is not a valid WS request.
         *                                  Nothing should be done when thrown, as the
         *                                  problem is already treated in the function
         *                                  (not idiomatic at all)
         * @throws HTTPException            if the received HTTP request is malformed
         * @throws IOException              for whatever other I/O exception
         */
        private void _handshake()
                throws HTTPException, IOException, NoSuchAlgorithmException,
                WebSocketException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this._connexion.getInputStream()));
            StringBuilder raw_request = new StringBuilder();

            String line = reader.readLine();
            while (line.length() > 0) {
                raw_request.append(line + "\n");
                line = reader.readLine();
            }

            // Parse the request to an HTTPRequest
            HTTPRequest request = HTTPRequest.parse(raw_request.toString(), true, true);

            this._HTTPVersion = request.getVersion();

            if (request.getMethod() != HTTPMethod.GET) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException(
                        "The request method should be GET, got " + request.getMethod() + " instead.");
            }

            if (!request.getLocation().equals("/")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException(
                        "The location of the request should be /, got " + request.getLocation() + " instead.");
            }

            if (request.getVersionDouble() < 1.1) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException("The HTTP version of the request shoud be at least 1.1, got version "
                        + request.getVersionDouble() + ".");
            }

            if (!request.getHeaders().containsKey("Host")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException("The request should contain a Host field, but is not present.");
            }

            if (!request.getHeaders().containsKey("Upgrade")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException("The request should contain an Upgrade field, but it is not present.");

            } else {

                if (!request.getHeaders().get("Upgrade").equals("websocket")) {
                    this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                    this._connexion.close();
                    throw new WebSocketException(
                            "The request should contain an Upgrade field containing the value websocket, but it contains the value \""
                                    + request.getHeaders().get("Upgrade") + "\".");

                }
            }

            if (!request.getHeaders().containsKey("Connection")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException("The request should contain a Connection field, but it is not present.");
            } else {
                if (!Arrays.asList(request.getHeaders().get("Connection").split(", ")).contains("Upgrade")) {
                    this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                    this._connexion.close();
                    throw new WebSocketException(
                            "The request should contain a Connection field containing the value Upgrade, but it contains the value \""
                                    + request.getHeaders().get("Connection") + "\".");

                }
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Key")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException(
                        "The request should contain a Sec-WebSocket-Key field, but it is not present.");
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Version")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException(
                        "The request should contain a Sec-WebSocket-Version field, but it is not present.");

            } else {

                if (!request.getHeaders().get("Sec-WebSocket-Version").equals("13")) {
                    this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                    this._connexion.close();
                    throw new WebSocketException(
                            "The request should contain a Sec-WebSocket-Version field containing the value 13, but it contains the value "
                                    + request.getHeaders().get("Sec-WebSocket-Version") + ".");

                }
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Protocol")) {
                this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                this._connexion.close();
                throw new WebSocketException(
                        "The request should contain a Sec-WebSocket-Protocol field, but it is not present.");

            } else {

                // Not conformant to the standard, but no important since the server will only
                // handle Scratchomised client connexions
                if (!request.getHeaders().get("Sec-WebSocket-Protocol").equals("scratchomised")) {
                    this._connexion.getOutputStream().write(this._get400HTTPResponse().getRawResponse().getBytes());
                    this._connexion.close();
                    throw new WebSocketException(
                            "The request should contain a Sec-WebSocket-Protocol field containing the value scratchomised, but it contains the value \""
                                    + request.getHeaders().get("Sec-WebSocket-Protocol") + "\".");

                }
            }

            // We can now assume that the received request is valid

            HashMap<String, String> response_headers = new HashMap<>();

            response_headers.put("Upgrade", "websocket");
            response_headers.put("Connection", request.getHeaders().get("Connection"));
            response_headers.put("Sec-WebSocket-Accept",
                    Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest(
                            (request.getHeaders().get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                    .getBytes("UTF-8"))));
            response_headers.put("Sec-WebSocket-Protocol", "scratchomised");

            HTTPResponse response = new HTTPResponse(this._HTTPVersion, 101, response_headers, null);
            this._connexion.getOutputStream().write(response.getRawResponse().getBytes());

        }

        private HTTPResponse _get400HTTPResponse() {
            return new HTTPResponse(this._HTTPVersion, 400, null,
                    "<!DOCTYPE html><html><head><title>400 bad request</title></head><body><h1>400 bad request</h1></body></html>");
        }

        /**
         * Send data to the client
         * 
         * @param data the data to be sent
         * 
         * @throws IOException
         */
        public void send(String data) throws IOException {
            // TODO
        }
    }
}