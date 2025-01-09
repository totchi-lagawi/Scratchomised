package io.github.totchi_lagawi.websocket_server;

/**
 * Enumeration of all WebSocket frame opcodes
 */
public enum WebSocketFrameOpcode {
    CONTINUATION_FRAME,
    TEXT_FRAME,
    BINARY_FRAME,
    // TODO "%x3-7 are reserved for further non-control frames"
    CONNECTION_CLOSE_FRAME,
    PING_FRAME,
    PONG_FRAME
    // TODO %xB-F are reserved for further control frames
}
