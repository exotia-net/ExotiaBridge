package net.exotia.bridge.proxy;

import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.proxy.client.EconomyListener;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.handlers.UserNeedUpdatePacketHandler;
import net.exotia.bridge.proxy.listeners.BungeePacketHandler;
import net.exotia.bridge.proxy.listeners.UserPostLoginListener;
import net.exotia.bridge.proxy.service.BungeeMessagingService;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.factory.ConfigurationFactory;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.plugin.Plugin;
import okhttp3.WebSocket;

public final class ProxyPlugin extends Plugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;
    private BungeeMessagingService bungeeMessagingService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getLogger());

        this.setupConfiguration();

        this.injector.registerInjectable(new MessagingPackCodec());
        MessagingService messagingService = new MessagingService();
        this.injector.registerInjectable(messagingService);
        this.bungeeMessagingService = this.injector.createInstance(BungeeMessagingService.class);
        this.injector.registerInjectable(this.bungeeMessagingService);

        this.setupBridge();

        messagingService.addListener(MessagingChannels.USER_NEED_UPDATE, this.injector.createInstance(UserNeedUpdatePacketHandler.class));

        for (String key : messagingService.getHandlers().keys()) {
            this.getProxy().registerChannel(key);
        }

        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(UserPostLoginListener.class));
        this.getProxy().getPluginManager().registerListener(this, this.injector.createInstance(BungeePacketHandler.class));
        ExotiaBridgeProvider.setProvider(this);
        this.bridge.pluginLoadedMessage(this.getLogger());
    }

    @Override
    public void onDisable() {
        this.bridge.stopHttpService();
    }

    private void setupBridge() {
        this.bridge = this.injector.createInstance(SetupBridge.class);
        this.userService = this.bridge.getUserService(this.bungeeMessagingService);

        HttpService httpService = this.bridge.getHttpService();
        WebSocket webSocket = httpService.prepareWebSocketConnection(this.bridge.getApiConfiguration(), this.injector.createInstance(EconomyListener.class));

        this.injector.registerInjectable(webSocket);
        this.injector.registerInjectable(this.userService);
    }

    private void setupConfiguration() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory(this.getDataFolder(), new YamlBungeeConfigurer());
        PluginConfiguration pluginConfiguration = configurationFactory.produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(pluginConfiguration);
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }
}
