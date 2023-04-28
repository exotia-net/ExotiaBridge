package net.exotia.bridge.plugin;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SetupBridge extends Bridge {
    @Inject private Plugin plugin;
    @Inject private PluginConfiguration configuration;

    @Override
    public ApiConfiguration getApiConfiguration() {
        return this.configuration;
    }
    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
}
