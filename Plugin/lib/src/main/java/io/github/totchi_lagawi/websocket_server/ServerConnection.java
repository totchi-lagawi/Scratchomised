package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPStatusCode;

import java.net.Socket;
import java.net.SocketAddress;

/**
 * Class representing a connection to a client
 */
public class ServerConnection {
    // The parent server, to call methods like onMessage() or onError()
    private WebSocketServer _server;
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
    private WebSocketConnectionState _state;
    // The subprotocol as requested by the client during the handshake
    private String _subprotocol;
    // The close code
    private int _close_code;
    // The close reason
    private String _close_reason;

    public void send(String message) {

    }

    public void send(byte[] message) {

    }

    public void close() {

    }

    public void wait_closed() {

    }

    public void ping() {

    }

    public void pong() {

    }

    public void respond(HTTPStatusCode status, String body) {

    }
}