package net.exotia.bridge.messaging_api.packets;

import net.exotia.bridge.messaging_api.MessagingPacket;

public class UserNeedUpdatePacket extends MessagingPacket {
    private final String serverId;
    private final String uuid;
    private final int balance;

    public UserNeedUpdatePacket(String serverId, String uuid, int balance) {
        this.serverId = serverId;
        this.uuid = uuid;
        this.balance = balance;
    }

    public UserNeedUpdatePacket() {
        this(null, null, 0);
    }

    public String getServerId() {
        return this.serverId;
    }
    public String getUuid() {
        return this.uuid;
    }
    public int getBalance() {
        return this.balance;
    }
}
