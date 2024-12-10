package io.github.totchi_lagawi.scratchomised_plugin;

import java.io.BufferedReader;
// import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;

import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPMethod;
import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPParser;
import io.github.totchi_lagawi.scratchomised_plugin.utils.http.HTTPRequest;

public class WebSocketServer implements Runnable {
    // Server's socket
    ServerSocket socket;
    // Holds the current connections
    Collection<Socket> connections;

    WebSocketServer(int port) throws IOException, BindException {
        // A server socket is assign as soon as the server class is instanciated
        // BindException is thrown if the socket can't be binded
        // Like, for example, if the specified port is inferior to 1024, but the user
        // isn't Administrator/root
        socket = new ServerSocket(port);
    }

    public void run() {
        while (true) {
            try {
                // Accept client connection
                Socket client = this.socket.accept();

                System.out.println(client.getRemoteSocketAddress() + " connected.");

                // Get the interfaces with input and output
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                // BufferedWriter out = new BufferedWriter(new
                // OutputStreamWriter(client.getOutputStream()));

                // Parse the request to an HTTPRequest
                HTTPRequest request = HTTPParser.parse(in, true, true);

                // Checking wether the request is a valid request

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

                if (!validity) {
                    throw new IllegalArgumentException();
                }

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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void stop() {
        // While the socket isn't closed
        while (!this.socket.isClosed()) {
            // We try to close it
            try {
                this.socket.close();
            } catch (IOException ex) {
                // Socket is busy, so we'll try again
            }
        }
    }

    public boolean isRunning() {
        // Prevent NullPointerException
        // Thanks null
        if (this.socket == null) {
            return false;
        }

        if (this.socket.isClosed()) {
            return false;
        } else {
            return true;
        }
    }

}