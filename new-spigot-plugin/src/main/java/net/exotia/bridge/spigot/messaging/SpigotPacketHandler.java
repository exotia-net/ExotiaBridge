package net.exotia.bridge.spigot.messaging;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class SpigotPacketHandler implements PluginMessageListener {
    @Inject private MessagingService messagingService;
    @Inject private MessagingPackCodec messagingPackCodec;

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        this.messagingService.getHandler(channel).forEach(handler -> {
            handler.handle(channel, this.messagingPackCodec.decode(bytes));
        });
    }
}
