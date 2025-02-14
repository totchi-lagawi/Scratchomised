package io.github.totchi_lagawi.scratchomised_plugin;

import java.net.BindException;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

public class PluginActionManageServer extends PluginAction {
    private LanguageManager _languageManager;
    // Store the server socket
    PluginServer server;
    // Store the server thread
    Thread server_thread;
    // Port to bind the server on
    int port = 55125;

    public PluginActionManageServer(LanguageManager languageManager) {
        this._languageManager = languageManager;
        // When instanciated, define the menu, name and enabled state of this action
        putPropertyValue(Property.NAME, this._languageManager.getString("server.name.start"));
        putPropertyValue(Property.MENU, this._languageManager.getString("plugin.menu"));
        putPropertyValue(Property.ENABLED, true);

    }

    @Override
    public void execute() {

        // Thanks null
        if (this.server == null) {
            try {
                this.server = new PluginServer(55125);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // And again thanks the one million dollars error
        if (this.server_thread == null) {
            try {
                this.server_thread = new Thread(this.server);
                this.server_thread.setName("Scratchomised - WebSocket server");
                this.server_thread.setDaemon(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }

        if (this.server_thread.isAlive()) {
            this.server.stop();
            // It is not possible to start a Thread more than once
            // So we need to recreate it next time
            this.server_thread = null;
            // And we should reinstance the server since the socket can't be reopened
            this.server = null;
            putPropertyValue(Property.NAME, this._languageManager.getString("server.name.start"));
        } else {
            this.server_thread.start();
            putPropertyValue(Property.NAME, this._languageManager.getString("server.name.stop"));
        }
    }
}