package net.exotia.bridge.plugin.service;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingPacket;
import net.exotia.bridge.messaging_api.MessagingService;
import net.exotia.bridge.shared.messaging.PluginMessagingService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class SpigotMessagingService extends PluginMessagingService {
    @Inject private Plugin plugin;
    @Inject private MessagingPackCodec messagingPackCodec;

    @Override
    public void registerChannel(String channel) {
        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channel);
    }

    @Override
    public <T> void runScheduler(T server, String channel, MessagingPacket messagingPacket) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            Player player = (Player) server;
            player.sendPluginMessage(this.plugin, channel, this.messagingPackCodec.encode(messagingPacket));
        }, 10);
    }

    @Override
    public Object getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}
//public class SpigotMessagingService extends MessagingService {
//    @Inject private Plugin plugin;
//    @Inject private MessagingPackCodec messagingPackCodec;
//
//    @PostConstruct
//    private void onConstruct() {
//        super.setConsumer(channel -> this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channel));
//    }
//
//    @Override
//    public <T> void sendMessageData(T server, String channel, MessagingPacket packet) {
//        super.sendMessageData(server, channel, packet);
//
//        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
//            Player player = (Player) server;
//            player.sendPluginMessage(this.plugin, channel, this.messagingPackCodec.encode(packet));
//        }, 10);
//    }
//}
