package io.github.totchi_lagawi.websocket_server;

public class WebSocketResponse<T> {
    public WebSocketResponseType type;
    public T data;

    public WebSocketResponse(WebSocketResponseType type, T data) {
        this.type = type;
        this.data = data;
    }
}