package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class PluginServerWebSocketHandler extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session sess) {
        System.out.println(sess.getRemoteAddress());
    }
}
