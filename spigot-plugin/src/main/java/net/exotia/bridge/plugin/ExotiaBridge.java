package net.exotia.bridge.plugin;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.api.ExotiaBridgeInstance;
import net.exotia.bridge.api.ExotiaBridgeProvider;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.factory.ConfigurationFactory;
import net.exotia.bridge.plugin.listeners.PlayerJoinListener;
import net.exotia.bridge.plugin.packets.SpigotPacketHandler;
import net.exotia.bridge.plugin.service.SpigotMessagingService;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.services.UserService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExotiaBridge extends JavaPlugin implements ExotiaBridgeInstance {
    private final OkaeriInjector injector = OkaeriInjector.create();
    private Bridge bridge;
    private UserService userService;

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.setupConfiguration();
        this.setupBridge();
        this.setupMessagingService();

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
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
        PluginConfiguration pluginConfiguration = new ConfigurationFactory(this.getDataFolder()).produce(PluginConfiguration.class, "configuration.yml");
        this.injector.registerInjectable(pluginConfiguration);
    }
    private void setupMessagingService() {
        this.injector.registerInjectable(new MessagingPackCodec());
        MessagingService messagingService = new MessagingService();
        this.injector.registerInjectable(messagingService);
        //messagingService.addListener(MessagingChannels.NEED_UPDATE, this.injector.createInstance(UserNeedUpdatePacketHandler.class));

        SpigotPacketHandler spigotPacketHandler = this.injector.createInstance(SpigotPacketHandler.class);

        for (String key : messagingService.getHandlers().keys()) {
            this.getServer().getMessenger().registerIncomingPluginChannel(this, key, spigotPacketHandler);
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.injector.registerInjectable(this.injector.createInstance(SpigotMessagingService.class));
    }

    @Override
    public ApiUserService getUserService() {
        return this.userService;
    }
}
