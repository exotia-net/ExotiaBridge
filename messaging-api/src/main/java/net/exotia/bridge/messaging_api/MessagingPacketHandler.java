package net.exotia.bridge.messaging_api;

public abstract class MessagingPacketHandler<T extends MessagingPacket> {
    public abstract void handle(String channel, T packet);
}
