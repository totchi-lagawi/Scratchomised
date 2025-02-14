package io.github.totchi_lagawi.scratchomised_plugin;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

public class PluginActionExportDatas extends PluginAction {
    private LanguageManager _languageManager;

    public PluginActionExportDatas(LanguageManager languageManager) {
        this._languageManager = languageManager;
        putPropertyValue(Property.NAME, this._languageManager.getString("export.name", null));
        putPropertyValue(Property.MENU, this._languageManager.getString("plugin.menu", null));
        putPropertyValue(Property.ENABLED, true);
    }

    @Override
    public void execute() {
        JOptionPane.showInputDialog("Hello world!");
    }
}
