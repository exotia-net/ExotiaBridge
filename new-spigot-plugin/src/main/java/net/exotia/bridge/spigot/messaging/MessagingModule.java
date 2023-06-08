package net.exotia.bridge.spigot.messaging;

import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.spigot.handlers.ReceivedTokenHandler;
import org.bukkit.plugin.Plugin;

public class MessagingModule {
    @Inject private Injector injector;
    @Inject private Plugin plugin;

    @PostConstruct
    public void onConstruct() {
        this.injector.registerInjectable(new MessagingPackCodec());
        MessagingService messagingService = new MessagingService();
        this.injector.registerInjectable(messagingService);

        SpigotPacketHandler spigotPacketHandler = this.injector.createInstance(SpigotPacketHandler.class);
        messagingService.addListener(MessagingChannels.SEND_TOKEN, this.injector.createInstance(ReceivedTokenHandler.class));

        for (String key : messagingService.getHandlers().keys()) {
            this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, key, spigotPacketHandler);
        }

        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        this.injector.registerInjectable(this.injector.createInstance(SpigotMessagingService.class));
    }
}
