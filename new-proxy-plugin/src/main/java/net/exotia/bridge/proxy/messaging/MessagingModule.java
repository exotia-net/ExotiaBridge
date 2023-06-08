package net.exotia.bridge.proxy.messaging;

import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.proxy.listeners.PlayerLoginListener;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class MessagingModule {
    @Inject private Injector injector;
    @Inject private Plugin plugin;

    @PostConstruct
    public void onConstruct() {
        this.injector.registerInjectable(new MessagingPackCodec());
        MessagingService messagingService = new MessagingService();
        this.injector.registerInjectable(messagingService);
        BungeeMessagingService bungeeMessagingService = this.injector.createInstance(BungeeMessagingService.class);
        this.injector.registerInjectable(bungeeMessagingService);

        /**
         * Tu rejestrujemy channele
         * messagingService.addListener();
         */
        for (String key : messagingService.getHandlers().keys()) {
            this.plugin.getProxy().registerChannel(key);
        }

        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this.injector.createInstance(BungeePacketHandler.class));
    }
}
