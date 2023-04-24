package net.exotia.bridge.plugin;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.factory.ConfigurationFactory;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.services.UserService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExotiaBridge extends JavaPlugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.setupConfiguration();
        this.setupBridge();

        ExotiaBridgeProvider.setProvider(this);
        //this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
    }

    @Override
    public void onDisable() {
        this.bridge.stopHttpService();
    }

    private void setupBridge() {
        this.bridge = this.injector.createInstance(SetupBridge.class);
        this.userService = this.bridge.getUserService();
        this.injector.registerInjectable(this.userService);
    }
    private void setupConfiguration() {
        PluginConfiguration pluginConfiguration = new ConfigurationFactory(this.getDataFolder()).produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(pluginConfiguration);
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }
}
