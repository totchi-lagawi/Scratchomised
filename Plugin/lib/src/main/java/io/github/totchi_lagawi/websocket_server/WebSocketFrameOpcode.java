package io.github.totchi_lagawi.websocket_server;

/**
 * Enumeration of all WebSocket frame opcodes
 */
public enum WebSocketFrameOpcode {
    CONTINUATION_FRAME,
    TEXT_FRAME,
    BINARY_FRAME,
    // %x3-7 are reserved for further non-control frames
    UNKNOWN_NON_CONTROL_FRAME,
    CONNECTION_CLOSE_FRAME,
    PING_FRAME,
    PONG_FRAME,
    // %xB-F are reserved for further control frames
    UNKNOWN_CONTROL_FRAME
}
