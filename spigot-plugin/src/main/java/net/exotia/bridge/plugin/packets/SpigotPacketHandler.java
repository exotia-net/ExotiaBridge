package net.exotia.bridge.plugin.packets;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.MessagingService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class SpigotPacketHandler implements PluginMessageListener {
    @Inject private MessagingService messagingService;
    @Inject private MessagingPackCodec messagingPackCodec;

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        for (MessagingPacketHandler messagingPacketHandler : this.messagingService.getHandler(channel)) {
            messagingPacketHandler.handle(channel, this.messagingPackCodec.decode(bytes));
        }
    }
}
