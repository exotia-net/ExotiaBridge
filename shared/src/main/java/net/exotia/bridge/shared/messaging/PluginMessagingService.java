package net.exotia.bridge.shared.messaging;

import net.exotia.bridge.messaging_api.MessagingPacket;
import net.exotia.bridge.messaging_api.MessagingService;
import java.util.UUID;

public abstract class PluginMessagingService extends MessagingService {
    public PluginMessagingService() {
        super.setConsumer(this::registerChannel);
    }

    public abstract void registerChannel(String channel);
    public abstract <T> void runScheduler(T server, String channel, MessagingPacket messagingPacket);
    public abstract Object getPlayer(UUID uuid);

    @Override
    public <T> void sendMessageData(T server, String channel, MessagingPacket messagingPacket) {
        super.sendMessageData(server, channel, messagingPacket);
        this.runScheduler(server, channel, messagingPacket);
    }
}
