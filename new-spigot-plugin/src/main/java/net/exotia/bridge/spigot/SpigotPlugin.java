package net.exotia.bridge.spigot;

import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiCalendarService;
import net.exotia.bridge.api.user.ApiEconomyService;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.factory.ConfigurationFactory;
import net.exotia.bridge.shared.services.CalendarService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.spigot.client.WebSocketClient;
import net.exotia.bridge.spigot.configuration.PluginConfiguration;
import net.exotia.bridge.spigot.listeners.PlayerJoinListener;
import net.exotia.bridge.spigot.messaging.MessagingModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlugin extends JavaPlugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;
    private CalendarService calendarService;
    private PluginConfiguration configuration;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getLogger());

        this.setupConfiguration();
        this.setupBridge();

        this.injector.createInstance(MessagingModule.class);

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);

        ExotiaBridgeProvider.setProvider(this);
        this.bridge.pluginLoadedMessage(this.getLogger());
    }

    @Override
    public void onDisable() {
        this.bridge.stopHttpService();
    }

    private void setupBridge() {
        this.bridge = new Bridge() {
            @Override
            public ApiConfiguration getApiConfiguration() {
                return configuration;
            }
        };
        this.userService = this.bridge.getUserService();
        this.injector.registerInjectable(this.userService);
        this.calendarService = this.bridge.getCalendarService();
        this.injector.registerInjectable(this.calendarService);
        this.userService.setupSocket(this.injector.createInstance(WebSocketClient.class));
    }
    private void setupConfiguration() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory(this.getDataFolder(),new YamlBukkitConfigurer(), new SerdesBukkit());
        this.configuration = configurationFactory.produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(this.configuration);
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }

    @Override
    public ApiEconomyService getEconomyService() {
        return this.bridge.getEconomyService();
    }

    @Override
    public ApiCalendarService getCalendarService() {
        return this.calendarService;
    }
}
