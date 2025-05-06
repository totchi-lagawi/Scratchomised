package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.eteks.sweethome3d.model.Home;

public class PluginServer implements Runnable {
    private LanguageManager _languageManager;
    private Home _home;
    private int _port;
    private Server _server;

    public PluginServer(int port, LanguageManager languageManager, Home home) {
        this._languageManager = languageManager;
        this._home = home;
        this._port = port;
        this._server = new Server(this._port);
    }

    @Override
    public void run() {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        this._server.setHandler(contextHandler);
        ServletHolder servletHolder = new ServletHolder(
                new PluginServerWebSocketServlet(this._languageManager, this._home));
        contextHandler.addServlet(servletHolder, "/");
        try {
            this._server.start();
        } catch (Exception ex) {
            System.err.println(this._languageManager.getString("log_prefix") + "Couldn't start the server :");
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (this.isRunning()) {
            try {
                this._server.stop();
            } catch (Exception ex) {
                System.err.println(this._languageManager.getString("log_prefix") + "Couldn't stop the server :");
                ex.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return this._server.isRunning();
    }

    public int getPort() {
        return this._port;
    }

    public void setPort(int port) {
        this._port = port;
    }
}
