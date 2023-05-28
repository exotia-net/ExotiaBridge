package net.exotia.bridge.spigot;

import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.factory.ConfigurationFactory;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.spigot.client.WebSocketClient;
import net.exotia.bridge.spigot.configuration.PluginConfiguration;
import net.exotia.bridge.spigot.listeners.PlayerJoinListener;
import okhttp3.WebSocket;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlugin extends JavaPlugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;
    private PluginConfiguration pluginConfiguration;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getLogger());

        this.setupConfiguration();
        this.setupBridge();

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);

        ExotiaBridgeProvider.setProvider(this);
        this.bridge.pluginLoadedMessage(this.getLogger());
    }

    @Override
    public void onDisable() {
        this.bridge.stopHttpService();
    }

    private void setupBridge() {
        this.bridge = this.injector.createInstance(SetupBridge.class);
        this.userService = this.bridge.getUserService(null);
        this.injector.registerInjectable(this.userService);
        this.userService.setupSocket(this.injector.createInstance(WebSocketClient.class));
        //this.injector.registerInjectable(webSocket);
    }
    private void setupConfiguration() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory(this.getDataFolder(), new YamlBukkitConfigurer(), new SerdesBukkit());
        this.pluginConfiguration = configurationFactory.produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(this.pluginConfiguration);
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }
}
