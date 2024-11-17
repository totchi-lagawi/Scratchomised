package io.github.totchi_lagawi.scratchomised_plugin;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

class ScratchomisedPluginActionManageServer extends PluginAction {
    public ScratchomisedPluginActionManageServer() {
        putPropertyValue(Property.NAME, "Hello world!");
        putPropertyValue(Property.MENU, "Scratchomised");
        putPropertyValue(Property.ENABLED, true);
    }

    @Override
    public void execute() {
        JOptionPane.showMessageDialog(null, "Hello world!");
    }
}