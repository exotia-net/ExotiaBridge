package net.exotia.bridge.proxy;

import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import okhttp3.WebSocket;

import java.util.UUID;

public final class ProxyPlugin extends Plugin {
    private Bridge bridge;
    private UserService userService;
    private final Injector injector = OkaeriInjector.create();

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.getLogger());
        this.injector.registerInjectable(new PluginConfiguration());
        this.setupBridge();
        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupBridge() {
        this.bridge = this.injector.createInstance(SetupBridge.class);
        this.userService = this.bridge.getUserService();
        this.injector.registerInjectable(this.userService);

//        HttpService httpService = this.bridge.getHttpService();
//        WebSocket webSocket = httpService.prepareWebSocketConnection(this.bridge.getApiConfiguration(), this.injector.createInstance(EconomyListener.class));
//
//        this.injector.registerInjectable(webSocket);
//        this.injector.registerInjectable(this.userService);
    }
}
