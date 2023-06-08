package net.exotia.bridge.proxy.messaging.handlers;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.packets.TokenPacket;
import net.exotia.bridge.proxy.configuration.TokenStorage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class VerifyTokenPacketHandler extends MessagingPacketHandler<TokenPacket> {
    @Inject private TokenStorage tokenStorage;
    @Inject private Plugin plugin;

    @Override
    public void handle(String channel, TokenPacket packet) {
        ProxiedPlayer player = this.plugin.getProxy().getPlayer(packet.getUuid());
        if (packet.getToken() != this.tokenStorage.getToken()) {
            player.disconnect("Niepoprawny token!");
        }
    }
}
