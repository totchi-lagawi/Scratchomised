package io.github.totchi_lagawi.websocket_server;

import io.github.totchi_lagawi.http_utils.HTTPRequest;

public class WebSocketRequest<T> {
    public T data;

    public WebSocketRequest(T data) {
        if (data instanceof HTTPRequest || data instanceof Frame) {
            this.data = data;
        } else {
            throw new IllegalArgumentException("Websocket response can only be an HTTP response or a Websocket frame");
        }
    }
}