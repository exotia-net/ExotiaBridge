package net.exotia.bridge.plugin;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.factory.ConfigurationFactory;
import net.exotia.bridge.plugin.listeners.PlayerJoinListener;
import net.exotia.bridge.shared.Bridge;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExotiaBridge extends JavaPlugin {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.setupConfiguration();
        this.setupBridge();

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
    }

    @Override
    public void onDisable() {
        this.bridge.stopHttpService();
    }

    private void setupBridge() {
        this.bridge = this.injector.createInstance(SetupBridge.class);
        this.injector.registerInjectable(this.bridge.getUserService());
    }
    private void setupConfiguration() {
        PluginConfiguration pluginConfiguration = new ConfigurationFactory(this.getDataFolder()).produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(pluginConfiguration);
    }
}
