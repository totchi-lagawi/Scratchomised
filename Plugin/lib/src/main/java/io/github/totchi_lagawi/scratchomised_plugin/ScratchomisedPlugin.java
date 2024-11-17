package io.github.totchi_lagawi.scratchomised_plugin;

import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;

public class ScratchomisedPlugin extends Plugin {
    @Override
    public PluginAction[] getActions() {
        return new PluginAction[] {new ScratchomisedPluginActionManageServer()};
    }
}