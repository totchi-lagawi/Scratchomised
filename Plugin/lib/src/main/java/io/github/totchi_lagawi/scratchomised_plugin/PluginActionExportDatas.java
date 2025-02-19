package io.github.totchi_lagawi.scratchomised_plugin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.plugin.PluginAction;

public class PluginActionExportDatas extends PluginAction implements PropertyChangeListener {
    private LanguageManager _languageManager;

    public PluginActionExportDatas(LanguageManager languageManager) {
        this._languageManager = languageManager;
        putPropertyValue(Property.NAME, this._languageManager.getString("menus.export"));
        putPropertyValue(Property.MENU, this._languageManager.getString("name"));
        putPropertyValue(Property.ENABLED, true);
    }

    @Override
    public void execute() {
        JOptionPane.showInputDialog("Hello world!");
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName() == "LANGUAGE") {
            putPropertyValue(Property.MENU, this._languageManager.getString("name"));
            putPropertyValue(Property.NAME, this._languageManager.getString("menus.export"));
        }
    }
}
