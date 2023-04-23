package net.exotia.bridge.proxy;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.listeners.UserPosLoginListener;
import net.exotia.bridge.shared.Bridge;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxyPlugin extends Plugin {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.setupConfiguration();
        this.setupBridge();

        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPosLoginListener.class));
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
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        this.injector.registerInjectable(pluginConfiguration);
    }
}
