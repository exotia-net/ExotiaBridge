package net.exotia.bridge.spigot.handlers;

import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.packets.TokenPacket;

public class ReceivedTokenHandler extends MessagingPacketHandler<TokenPacket> {
    @Override
    public void handle(String channel, TokenPacket packet) {
        System.out.println("==================");
        System.out.println(channel + " " + packet.getToken());
        System.out.println(channel + " " + packet.getUuid());
        System.out.println("==================");
    }
}
