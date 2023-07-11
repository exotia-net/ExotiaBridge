package net.exotia.bridge.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiCalendarService;
import net.exotia.bridge.api.user.ApiEconomyService;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.configuration.proxy.ProxyConfiguration;
import net.exotia.bridge.shared.factory.ConfigurationFactory;
import net.exotia.bridge.shared.factory.FactoryPlatform;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.velocity.listeners.UserPostLoginListener;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Plugin(
        id = "exotiabridge",
        name = "ExotiaBridge-velocity",
        version = "1.0-SNAPSHOT"
)
public class VelocityPlugin implements ExotiaBridgeInstance  {
    private final Injector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;

    @Inject private Logger logger;
    @Inject private ProxyServer proxyServer;
    @Inject private @DataDirectory Path dataDirectory;
    @Inject private PluginDescription pluginDescription;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.logger);
        this.injector.registerInjectable(this.injector);

        /**
         * Setup Configuration
         */
        this.createPluginDataFolder();
        ConfigurationFactory configurationFactory = new ConfigurationFactory(this.dataDirectory.toFile());
        ProxyConfiguration proxyConfiguration = configurationFactory.produce(FactoryPlatform.VELOCITY, ProxyConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(proxyConfiguration);

        EventManager eventManager = this.proxyServer.getEventManager();
        eventManager.register(this, this.injector.createInstance(UserPostLoginListener.class));
        this.logger.info("Successfully registered listeners.");

        this.bridge = new Bridge() {
            @Override
            public ApiConfiguration getApiConfiguration() {
                return proxyConfiguration;
            }
        };
        this.userService = this.bridge.getUserService();
        this.injector.registerInjectable(userService);

        ExotiaBridgeProvider.setProvider(this);
    }

    private void createPluginDataFolder() {
        File pluginDataFolder = new File(this.dataDirectory.toFile(), "data");
        if (!pluginDataFolder.exists() && pluginDataFolder.mkdirs()) {
            this.logger.info("Successfully created data folder.");
        }
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
        return this.bridge.getCalendarService();
    }
}
