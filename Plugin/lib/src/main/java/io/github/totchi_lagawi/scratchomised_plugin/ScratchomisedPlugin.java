package io.github.totchi_lagawi.scratchomised_plugin;

import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.UserPreferences.Property;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;

public class ScratchomisedPlugin extends Plugin {
    private LanguageManager _languageManager;

    @Override
    public PluginAction[] getActions() {
        UserPreferences userPreferences = getUserPreferences();
        this._languageManager = new LanguageManager(userPreferences.getLanguage());
        PluginActionManageServer pluginActionManageServer = new PluginActionManageServer(this._languageManager);
        PluginActionExportDatas pluginActionExportDatas = new PluginActionExportDatas(this._languageManager);
        userPreferences.addPropertyChangeListener(Property.LANGUAGE, this._languageManager);
        userPreferences.addPropertyChangeListener(Property.LANGUAGE, pluginActionManageServer);
        userPreferences.addPropertyChangeListener(Property.LANGUAGE, pluginActionExportDatas);
        return new PluginAction[] {
                pluginActionManageServer,
                pluginActionExportDatas
        };
    }
}