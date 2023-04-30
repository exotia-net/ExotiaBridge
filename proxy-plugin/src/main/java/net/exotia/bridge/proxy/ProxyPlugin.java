package net.exotia.bridge.proxy;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.handlers.UserNeedUpdatePacketHandler;
import net.exotia.bridge.proxy.listeners.BungeePacketHandler;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.proxy.service.UpdatableService;
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

        this.injector.registerInjectable(new MessagingPackCodec());
        MessagingService messagingService = new MessagingService();
        this.injector.registerInjectable(messagingService);

        this.injector.registerInjectable(this.injector.createInstance(UpdatableService.class));

        messagingService.addListener(MessagingChannels.USER_NEED_UPDATE, this.injector.createInstance(UserNeedUpdatePacketHandler.class));

        for (String key : messagingService.getHandlers().keys()) {
            this.getProxy().registerChannel(key);
        }

        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));
        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(BungeePacketHandler.class));
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
