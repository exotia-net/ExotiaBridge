package net.exotia.bridge.proxy.service;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPackCodec;
import net.exotia.bridge.messaging_api.MessagingPacket;
import net.exotia.bridge.shared.messaging.PluginMessagingService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class BungeeMessagingService extends PluginMessagingService {
    @Inject private MessagingPackCodec messagingPackCodec;
    @Inject private Plugin plugin;

    @Override
    public void registerChannel(String channel) {
        this.plugin.getProxy().registerChannel(channel);
    }

    @Override
    public <T> void runScheduler(T object, String channel, MessagingPacket messagingPacket) {
        Server server = (Server) object;
        ServerInfo serverInfo = server.getInfo();
        if (serverInfo.getPlayers().isEmpty()) {
            super.sendMessageData(object, channel, messagingPacket);
            return;
        }
        serverInfo.sendData(channel, this.messagingPackCodec.encode(messagingPacket));
    }

    @Override
    public Object getPlayer(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid);
    }
}
