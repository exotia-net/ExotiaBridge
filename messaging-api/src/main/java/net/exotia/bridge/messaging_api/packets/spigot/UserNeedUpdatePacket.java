package net.exotia.bridge.messaging_api.packets.spigot;

import net.exotia.bridge.messaging_api.MessagingPacket;

public class UserNeedUpdatePacket extends MessagingPacket {
    private final String username;

    public UserNeedUpdatePacket(String username) {
        this.username = username;
    }

    public UserNeedUpdatePacket() {
        this(null);
    }

    public String getUsername() {
        return this.username;
    }
}
