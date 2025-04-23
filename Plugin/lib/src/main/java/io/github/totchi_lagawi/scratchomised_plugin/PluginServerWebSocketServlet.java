package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class PluginServerWebSocketServlet extends WebSocketServlet {
    private LanguageManager _languageManager;

    public PluginServerWebSocketServlet(LanguageManager languageManager) {
        this._languageManager = languageManager;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(PluginServerWebSocketEndpoint.class);
        factory.setCreator(new PluginServerWebSocketCreator(this._languageManager));
    }

}
