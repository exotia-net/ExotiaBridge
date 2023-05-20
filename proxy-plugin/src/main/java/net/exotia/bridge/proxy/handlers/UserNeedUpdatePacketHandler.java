package net.exotia.bridge.proxy.handlers;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.packets.UserNeedUpdatePacket;
import net.exotia.bridge.shared.services.UserService;
import okhttp3.WebSocket;

public class UserNeedUpdatePacketHandler extends MessagingPacketHandler<UserNeedUpdatePacket> {
    @Inject private UserService userService;
    @Inject private WebSocket webSocket;

    @Override
    public void handle(String channel, UserNeedUpdatePacket packet) {
        this.webSocket.send(String.format("/servers/%s/economy %s %s", packet.getServerId(), packet.getUuid(), packet.getBalance()));
    }
}