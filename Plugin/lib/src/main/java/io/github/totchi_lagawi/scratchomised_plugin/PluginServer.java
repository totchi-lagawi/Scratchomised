package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class PluginServer implements Runnable {
    private LanguageManager _languageManager;
    private int _port;
    private Server _server;

    public PluginServer(int port, LanguageManager languageManager) {
        this._port = port;
        this._languageManager = languageManager;
    }

    @Override
    public void run() {
        Log.setLog(new DummyLogger());
        this._server = new Server(this._port);

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(PluginServerWebSocketHandler.class);
            }
        };

        this._server.setHandler(wsHandler);

        Thread.currentThread().setContextClassLoader(PluginServer.class.getClassLoader());

        try {
            this._server.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            this._server.join();
        } catch (InterruptedException ex) {
            return;
        }
    }

    public void stop() {
        try {
            this._server.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isRunning() {
        return this._server.isRunning();
    }
}
