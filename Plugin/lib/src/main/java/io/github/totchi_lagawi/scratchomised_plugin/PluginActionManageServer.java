package io.github.totchi_lagawi.scratchomised_plugin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

public class PluginActionManageServer extends PluginAction implements PropertyChangeListener, UncaughtExceptionHandler {
    private LanguageManager _languageManager;
    // Store the server socket
    private PluginServer _server;
    // Store the server thread
    private Thread _server_thread;
    // Port to bind the server on
    private int _port = 55125;

    public PluginActionManageServer(LanguageManager languageManager) {
        this._languageManager = languageManager;
        // When instanciated, define the menu, name and enabled state of this action
        putPropertyValue(Property.NAME, this._languageManager.getString("menus.server.start"));
        putPropertyValue(Property.MENU, this._languageManager.getString("name"));
        putPropertyValue(Property.ENABLED, true);

    }

    @Override
    public void execute() {
        if (this.isServerRunning()) {
            this.stopServer();
        } else {
            this.startServer();
        }
    }

    /**
     * Start the server, stopping it if already running.
     * Make sure to check wether it is running before trying to start it.
     */
    public void startServer() {
        // Stop and clean the server thread and the server if needed
        if (this.isServerRunning()) {
            this.stopServer();
        }

        // Start by instanciating the server
        try {
            this._server = new PluginServer(55125, this._languageManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // As well as the server thread
        try {
            this._server_thread = new Thread(this._server);
            this._server_thread.setName("Scratchomised - WebSocket server");
            this._server_thread.setUncaughtExceptionHandler(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        // And we say that the server is started
        // We have to say that it is started before actually starting it as of a
        // concurrency bug if the server instantly crashes
        // If it does, then :
        // - The server is started
        // - It throws an Exception, which is caught
        // - this.stopServer() is called
        // - The name is changed as if the server was stopped (which is the case)
        // - But now this.startServer() finishes executing and change the name as if the
        // server was running, which is not the case
        // Sometimes, this.startServer() finishes executing before this.stopServer(),
        // and so the name is correct. But sometimes not, that's why the name should be
        // changed before it ever could be changed again
        putPropertyValue(Property.NAME, this._languageManager.getString("menus.server.stop"));

        // Then we start the server
        this._server_thread.start();
    }

    /**
     * Stop the server if needed, and clean things a bit.
     */
    public void stopServer() {
        // Properly stop the server ONLY when correctly running
        if (this.isServerRunning()) {
            this._server.stop();
        }
        // It is not possible to start a Thread more than once
        // So we need to recreate it next time
        this._server_thread = null;
        // And we should reinstance the server since the socket can't be reopened
        this._server = null;

        // Now we say that the server is stopped
        putPropertyValue(Property.NAME, this._languageManager.getString("menus.server.start"));
    }

    /**
     * Returns whether the server is currently running.
     * 
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    public boolean isServerRunning() {
        if (this._server != null && this._server_thread != null && this._server_thread.isAlive()) {
            return true;
        } else {
            return false;
        }
    }

    // Normally called in case of a language change
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName() == "LANGUAGE") {
            putPropertyValue(Property.MENU, this._languageManager.getString("name"));

            if (this.isServerRunning()) {
                putPropertyValue(Property.NAME, this._languageManager.getString("menus.server.stop"));
            } else {
                putPropertyValue(Property.NAME, this._languageManager.getString("menus.server.start"));
            }
        }
    }

    // Normally called in case of an error in the server thread
    public void uncaughtException(Thread thread, Throwable exception) {
        // We've got an exception from the server : it means it crashed
        // Exceptions from connexion handler are handled differently
        // We should then clean the server and its thread
        this.stopServer();
        JOptionPane.showMessageDialog(null, exception.getMessage(),
                this._languageManager.getString("name") + " - " + this._languageManager.getString("errors.error"),
                JOptionPane.ERROR_MESSAGE);
    }
}