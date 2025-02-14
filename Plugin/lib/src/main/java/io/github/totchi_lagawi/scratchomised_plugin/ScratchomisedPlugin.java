package io.github.totchi_lagawi.scratchomised_plugin;

import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;

public class ScratchomisedPlugin extends Plugin {
    private LanguageManager _languageManager;

    public ScratchomisedPlugin() {
        this._languageManager = new LanguageManager();
        this._languageManager.setLang(getUserPreferences().getLanguage());
    }

    @Override
    public PluginAction[] getActions() {
        return new PluginAction[] {
                new PluginActionManageServer(this._languageManager),
                new PluginActionExportDatas(this._languageManager)
        };
    }
}