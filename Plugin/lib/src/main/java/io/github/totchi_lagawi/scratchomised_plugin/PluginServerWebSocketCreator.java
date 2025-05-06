package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import com.eteks.sweethome3d.model.Home;

public class PluginServerWebSocketCreator implements WebSocketCreator {
    private LanguageManager _languageManager;
    private Home _home;

    public PluginServerWebSocketCreator(LanguageManager languageManager, Home home) {
        this._languageManager = languageManager;
        this._home = home;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        if (req.hasSubProtocol("scratchomised")) {
            return new PluginServerWebSocketEndpoint(this._languageManager, this._home);
        } else {
            return null;
        }
    }

}
