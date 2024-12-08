package io.github.totchi_lagawi.scratchomised_plugin;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

public class PluginActionExportDatas extends PluginAction {
    public PluginActionExportDatas() {
        putPropertyValue(Property.NAME, LanguageManager.getString("export.name", null));
        putPropertyValue(Property.MENU, LanguageManager.getString("plugin.menu", null));
        putPropertyValue(Property.ENABLED, true);
    }

    @Override
    public void execute() {
        JOptionPane.showInputDialog("Hello world!");
    }
}
