package net.exotia.bridge.proxy.handlers;

import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.packets.spigot.UserNeedUpdatePacket;

public class UserNeedUpdatePacketHandler extends MessagingPacketHandler<UserNeedUpdatePacket> {
    @Override
    public void handle(String channel, UserNeedUpdatePacket packet) {
        System.out.println("=======================");
        System.out.println(channel + ": " + packet.getUsername());
        System.out.println("=======================");
    }
}