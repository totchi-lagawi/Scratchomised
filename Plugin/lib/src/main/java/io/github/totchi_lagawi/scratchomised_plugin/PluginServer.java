package io.github.totchi_lagawi.scratchomised_plugin;

import java.io.IOException;

import io.github.totchi_lagawi.websocket_server.Server;
import io.github.totchi_lagawi.websocket_server.ServerConnexion;

public class PluginServer extends Server {
    private LanguageManager _languageManager;

    public PluginServer(int port, LanguageManager languageManager) throws IOException {
        super(port);
        this._languageManager = languageManager;
    }

    @Override
    protected void onOpen(ServerConnexion connexion) {
        System.out.println(
                this._languageManager.getString("log_prefix") + "client connected : "
                        + connexion.getRemoteAddress());
    }

    @Override
    protected void onMessage(ServerConnexion connexion, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    @Override
    protected void onMessage(ServerConnexion connexion, byte[] message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    @Override
    protected void onClose(ServerConnexion connexion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    protected void onError(ServerConnexion connexion, Exception exception) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onError'");
    }

}
