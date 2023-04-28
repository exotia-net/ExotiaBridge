package net.exotia.bridge.proxy;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxyPlugin extends Plugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.setupConfiguration();
        this.setupBridge();

        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));
        ExotiaBridgeProvider.setProvider(this);
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
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        this.injector.registerInjectable(pluginConfiguration);
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }
}
