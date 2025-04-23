package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import org.yaml.snakeyaml.Yaml;

public class PluginServerWebSocketEndpoint implements WebSocketListener {
    private LanguageManager _languageManager;
    private Session _session;

    public PluginServerWebSocketEndpoint(LanguageManager languageManager) {
        this._languageManager = languageManager;
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println(this._languageManager.getString("log_prefix") + this._session.getRemoteAddress()
                + " disconnected (reason : "
                + reason + ")");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this._session = session;
        System.out.println(this._languageManager.getString("log_prefix") + session.getRemoteAddress() + " connected");
    }

    @Override
    public void onWebSocketError(Throwable cause) {

    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    @Override
    public void onWebSocketText(String message) {
        Yaml yaml = new Yaml();
        ScratchomisedRequest request = yaml.loadAs(message, ScratchomisedRequest.class);
        System.out.println(request.action + ", " + request.args);
    }
}
