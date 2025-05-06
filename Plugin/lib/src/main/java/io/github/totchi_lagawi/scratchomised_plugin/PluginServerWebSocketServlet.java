package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.eteks.sweethome3d.model.Home;

public class PluginServerWebSocketServlet extends WebSocketServlet {
    private LanguageManager _languageManager;
    private Home _home;

    public PluginServerWebSocketServlet(LanguageManager languageManager, Home home) {
        this._languageManager = languageManager;
        this._home = home;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(PluginServerWebSocketEndpoint.class);
        factory.setCreator(new PluginServerWebSocketCreator(this._languageManager, this._home));
    }

}
