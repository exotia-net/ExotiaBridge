package net.exotia.bridge.plugin;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.factory.ConfigurationFactory;
import net.exotia.bridge.plugin.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExotiaBridge extends JavaPlugin {
    private final OkaeriInjector injector = OkaeriInjector.create();

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        PluginConfiguration pluginConfiguration = new ConfigurationFactory(this.getDataFolder()).produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(pluginConfiguration);

//        this.injector.registerInjectable(this.injector.createInstance(HttpService.class));
//        this.injector.registerInjectable(this.injector.createInstance(UserService.class));
        Test test = this.injector.createInstance(Test.class);
        this.injector.registerInjectable(test.getUserService());

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
