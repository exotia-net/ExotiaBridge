package net.exotia.bridge.plugin.service;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingPacket;
import net.exotia.bridge.messaging_api.MessagingService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotMessagingService extends MessagingService {
    @Inject private Plugin plugin;
    @Inject private MessagingPackCodec messagingPackCodec;

    @PostConstruct
    private void onConstruct() {
        super.setConsumer(channel -> this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channel));
    }

    @Override
    public <T> void sendMessageData(T server, String channel, MessagingPacket packet) {
        super.sendMessageData(server, channel, packet);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            Player player = (Player) server;
            player.sendPluginMessage(this.plugin, channel, this.messagingPackCodec.encode(packet));
        }, 10);
    }
}
