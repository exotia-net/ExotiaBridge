package net.exotia.bridge.proxy;

import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiEconomyService;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.factory.ConfigurationFactory;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxyPlugin extends Plugin implements ExotiaBridgeInstance {
    private Bridge bridge;
    private final Injector injector = OkaeriInjector.create();
    private PluginConfiguration configuration;
    private UserService userService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.getLogger());
        this.setupConfiguration();
        this.setupBridge();
        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));

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
        this.injector.registerInjectable(userService);
    }
    private void setupConfiguration() {
        ConfigurationFactory factory = new ConfigurationFactory(this.getDataFolder(), new YamlBungeeConfigurer());
        this.configuration = factory.produce(PluginConfiguration.class, "configuration.yml");
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
}
