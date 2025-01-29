package io.github.totchi_lagawi.websocket_server;

/**
 * Enum of the different WebSocket connection states
 */
public enum ConnectionState {
    CLOSED,
    CLOSING,
    CONNECTING,
    OPEN
}