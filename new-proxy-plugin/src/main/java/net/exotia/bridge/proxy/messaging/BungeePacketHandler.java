package net.exotia.bridge.proxy.messaging;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeePacketHandler implements Listener {
    @Inject private MessagingService messagingService;
    @Inject private MessagingPackCodec messagingPackCodec;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMessage(PluginMessageEvent event) {
        String channel = event.getTag();
        this.messagingService.getHandler(channel).forEach(handler -> {
            handler.handle(channel, this.messagingPackCodec.decode(event.getData()));
        });
    }
}
