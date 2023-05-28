package net.exotia.bridge.proxy;

import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxyPlugin extends Plugin {
    private Bridge bridge;
    private final Injector injector = OkaeriInjector.create();
    private PluginConfiguration configuration;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.getLogger());
        this.configuration = new PluginConfiguration();
        this.injector.registerInjectable(this.configuration);
        this.setupBridge();
        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));
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
        UserService userService = this.bridge.getUserService();
        this.injector.registerInjectable(userService);
    }
}
