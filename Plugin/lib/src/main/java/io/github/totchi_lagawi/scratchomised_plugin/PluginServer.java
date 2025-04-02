package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.util.log.Log;

import io.javalin.Javalin;
import io.javalin.core.security.RouteRole;

public class PluginServer implements Runnable {
    private LanguageManager _languageManager;
    private int _port;
    private Javalin _server;

    public PluginServer(int port, LanguageManager languageManager) {
        this._port = port;
        this._languageManager = languageManager;
    }

    @Override
    public void run() {
        // Shut up Jetty, unless you have something important to say
        System.setProperty("org.eclipse.jetty.util.log.announce", "false");
        Log.setLog(new PluginServerLogger(this._languageManager));

        // Hello Javalin!
        this._server = Javalin.create();
        WebSocketMessageHandler messageHandler = new WebSocketMessageHandler(this._languageManager);
        this._server.ws("/", ws -> {
            ws.onConnect(ctx -> System.out.println(this._languageManager.getString("log_prefix")
                    + ctx.session.getRemoteAddress().toString() + " connected"));
            ws.onMessage(messageHandler);
        });

        // Needed due to a problem with the class loader
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        this._server.start(this._port);
    }

    public void stop() {
        this._server.close();
    }

    public boolean isRunning() {
        return this._server.jettyServer().server().isRunning();
    }
}
