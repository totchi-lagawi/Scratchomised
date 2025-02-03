package io.github.totchi_lagawi.scratchomised_plugin;

import io.github.totchi_lagawi.websocket_server.Server;
import io.github.totchi_lagawi.websocket_server.ServerConnection;

public class PluginServer extends Server {

    public PluginServer(int port) {
        super(port);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onOpen(ServerConnection connection) {
        System.out.println(LanguageManager.getString("log_prefix", null) + " client connected : " + connection.);
    }

    @Override
    protected void onMessage(ServerConnection connection, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    @Override
    protected void onMessage(ServerConnection connection, byte[] message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    @Override
    protected void onClose(ServerConnection connection) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    protected void onError(ServerConnection connection, Exception exception) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onError'");
    }

}
