package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class PluginServerWebSocketCreator implements WebSocketCreator {
    private LanguageManager _languageManager;

    public PluginServerWebSocketCreator(LanguageManager languageManager) {
        this._languageManager = languageManager;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        if (req.hasSubProtocol("scratchomised")) {
            return new PluginServerWebSocketEndpoint(this._languageManager);
        } else {
            return null;
        }
    }

}
