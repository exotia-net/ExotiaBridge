package net.exotia.bridge.proxy;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.md_5.bungee.api.plugin.Plugin;

public class SetupBridge extends Bridge {
    @Inject private Plugin plugin;
    @Inject private PluginConfiguration configuration;

    @Override
    public ApiConfiguration getApiConfiguration() {
        return this.configuration;
    }
    @Override
    public void runAsync(Runnable runnable) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, runnable);
    }
}
