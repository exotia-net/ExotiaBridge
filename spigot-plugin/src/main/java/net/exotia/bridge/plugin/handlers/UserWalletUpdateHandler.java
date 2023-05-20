package net.exotia.bridge.plugin.handlers;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.MessagingPacketHandler;
import net.exotia.bridge.messaging_api.packets.UpdateWalletPacket;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.User;

import java.util.UUID;

public class UserWalletUpdateHandler extends MessagingPacketHandler<UpdateWalletPacket> {
    @Inject private UserService userService;

    @Override
    public void handle(String channel, UpdateWalletPacket packet) {
        User user = this.userService.getUser(UUID.fromString(packet.getUuid()));
        if (user == null) return;
        user.setCoins(packet.getValue());
    }
}
