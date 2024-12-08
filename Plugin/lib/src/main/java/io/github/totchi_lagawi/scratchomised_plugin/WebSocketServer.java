package io.github.totchi_lagawi.scratchomised_plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                // TODO
                // Refactor this to be cleaner

                // Translate the input datas to a String
                StringBuilder data_buffer = new StringBuilder();
                String s;
                while ((s = in.readLine()) != null) {
                    data_buffer.append(s + "\n");
                    if (s.isEmpty()) {
                        break;
                    }
                }

                String data = data_buffer.toString();

                // TODO
                // Parse the request to an object, instead of just doing little hacks that "works"
                // This would allow :
                //         - Easier manipulation of the request
                //         - Easier compliance with https://datatracker.ietf.org/doc/html/rfc6455.html

                Matcher get = Pattern.compile("^GET").matcher(data);

                // Check wether the request it a GET request
                if (get.find()) {
                    System.out.println("Request is a valid GET request");
                    Matcher key = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    key.find();
                    String response = ""
                            + "HTTP/1.1 101 Switching Protocols\n"
                            + "Connection: Upgrade\n"
                            + "Upgrade: websocket\n"
                            + "Sec-WebSocket-Accept: "
                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
                                    .digest((key.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")));
                    System.out.println("Response : \n" + response);
                    out.write(response);
                } else {
                    // We assume that the connection is a WebSocket request
                    System.out.println("Looks like a WebSocket request!");
                    
                }
            } catch (SocketException ex) {
                // SocketException is thrown when socket.accept() is called on a closed socket
                // which means that socket.close() was called on that socket
                // which means that the server should be stopped
                // We should then just return, to stop the thread
                return;
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