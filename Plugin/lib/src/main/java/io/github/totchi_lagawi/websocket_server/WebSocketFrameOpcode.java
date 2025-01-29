package io.github.totchi_lagawi.websocket_server;

public enum WebSocketFrameOpcode {
    // 0x0
    CONTINUATION_FRAME,
    // 0x1
    TEXT_FRAME,
    // 0x2
    BINARY_FRAME,
    // 0x3 -> 0x7
    UNKNOWN_NON_CONTROL_FRAME,
    // 0x8
    CLOSE_FRAME,
    // 0x9
    PING_FRAME,
    // 0xA
    PONG_FRAME,
    // 0xB -> 0xF
    UNKNOWN_CONTROL_FRAME
}
