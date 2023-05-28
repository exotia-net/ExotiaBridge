package net.exotia.bridge.spigot;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.spigot.configuration.PluginConfiguration;

public class SetupBridge extends Bridge {
    @Inject private PluginConfiguration configuration;

    @Override
    public ApiConfiguration getApiConfiguration() {
        return this.configuration;
    }

    @Override
    public void runAsync(Runnable runnable) {
        //this.plugin.getProxy().getScheduler().runAsync(this.plugin, runnable);
    }
}
