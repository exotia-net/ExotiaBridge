package net.exotia.bridge.messaging_api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MessagingService {
    private final Multimap<String, MessagingPacketHandler<?>> handlers = ArrayListMultimap.create();
    private final Set<String> subscribedChannels = new HashSet<>();
    private Consumer<String> subscribeConsumer;

    public <T> void sendMessageData(T server, String channel, MessagingPacket packet) {
        if(this.subscribedChannels.contains(channel)) {
            return;
        }
        this.subscribeConsumer.accept(channel);
        this.subscribedChannels.add(channel);
    }

    public void setConsumer(Consumer<String> subscribeConsumer) {
        this.subscribeConsumer = subscribeConsumer;
    }
    public void addListener(String channel, MessagingPacketHandler<?> handler) {
        this.handlers.put(channel, handler);
    }
    public Collection<MessagingPacketHandler<?>> getHandler(String channel) {
        if (!this.handlers.containsKey(channel)) {
            return Collections.emptyList();
        }
        return this.handlers.get(channel);
    }
    public Multimap<String, MessagingPacketHandler<?>> getHandlers() {
        return this.handlers;
    }
}