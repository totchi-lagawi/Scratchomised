package io.github.totchi_lagawi.scratchomised_plugin.utils.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import io.github.totchi_lagawi.scratchomised_plugin.LanguageManager;
import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPMethod;
import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPParser;
import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPRequest;

public class WebSocketServerConnexionHandler implements Runnable {
    // The socket to the client
    private Socket _connexion;
    // The server class
    // Useful when calling "onMessage" and other functions
    private WebSocketServer _server;

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
     * Run the connexion handler
     */
    public void run() {
        try {

            System.err.println("Parsing request");

            // Parse the request to an HTTPRequest
            HTTPRequest request = HTTPParser.parseRequest(
                    new BufferedReader(new InputStreamReader(this._connexion.getInputStream())), true, true);

            System.err.println("Parsed!");

            // Check wether the request is a valid request
            boolean validity = true;

            if (request.getMethod() != HTTPMethod.GET) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request method should be GET, got " + request.getMethod() + " instead.");
                validity = false;
            }

            if (!request.getLocation().equals("/")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the location of the request should be /, got " + request.getLocation() + " instead.");
                validity = false;
            }

            if (request.getVersionDouble() < 1.1) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the HTTP version of the request shoud be at least 1.1, got version "
                        + request.getVersionDouble());
                validity = false;
            }

            if (!request.getHeaders().containsKey("Host")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Host field, but is not present.");
                validity = false;
            }

            if (!request.getHeaders().containsKey("Upgrade")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain an Upgrade field, but it is not present.");
                validity = false;
            }

            if (!request.getHeaders().get("Upgrade").equals("websocket")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain an Upgrade field containing the value websocket, but it contains the value "
                        + request.getHeaders().get("Upgrade") + ".");
                validity = false;
            }

            if (!request.getHeaders().containsKey("Connection")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Connection field, but it is not present");
                validity = false;
            }

            if (!request.getHeaders().get("Connection").equals("Upgrade")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Connection field containing the value Upgrade, but it contains the value "
                        + request.getHeaders().get("Connection") + ".");
                validity = false;
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Key")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Sec-WebSocket-Key field, but it is not present");
                validity = false;
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Version")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Sec-WebSocket-Version field, but it is not present");
                validity = false;
            }

            if (!request.getHeaders().get("Sec-WebSocket-Version").equals("13")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Sec-WebSocket-Version field containing the value 13, but it contains the value "
                        + request.getHeaders().get("Sec-WebSocket-Version") + ".");
                validity = false;
            }

            if (!request.getHeaders().containsKey("Sec-WebSocket-Protocol")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Sec-WebSocket-Protocol field, but it is not present");
                validity = false;
            }

            if (!request.getHeaders().get("Sec-WebSocket-Protocol").equals("scratchomised")) {
                System.err.println(LanguageManager.getString("log_prefix", null)
                        + "the request should contain a Sec-WebSocket-Protocol field containing the value scratchomised, but it contains the value "
                        + request.getHeaders().get("Sec-WebSocket-Protocol") + ".");
                validity = false;
            }

            // DEBUG
            System.err.println("Test ended");

            if (!validity) {
                System.err.println("Invalid request");
                throw new IllegalArgumentException();
            }

            // The request is a valid request
            // TODO
            // Complete the handshake
            // Implements WebSocket data sending/receiving

        } catch (SocketException ex) {
            // SocketException is thrown when socket.accept() is called on a closed socket
            // which means that socket.close() was called on that socket
            // which means that the server should be stopped
            // We should then just return, to stop the thread
            return;
        } catch (IllegalArgumentException ex) {
            // This is not idiomatic at all
            // It means that the request is not a valid WebSocket request, hence we should
            // do nothing, as it was treated above
            System.err.println("In IllegalArgumentException");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send data to the client
     * 
     * @param data the data to be sent
     * 
     * @throws IOException
     */
    public void send(String data) throws IOException {
        this._connexion.getOutputStream().write(data.getBytes("UTF-8"));
    }
}