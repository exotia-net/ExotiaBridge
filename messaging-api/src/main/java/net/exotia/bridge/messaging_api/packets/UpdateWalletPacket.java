package net.exotia.bridge.messaging_api.packets;

import net.exotia.bridge.messaging_api.MessagingPacket;

public class UpdateWalletPacket extends MessagingPacket {
    private final String uuid;
    private final float value;

    public UpdateWalletPacket(String uuid, float value) {
        this.uuid = uuid;
        this.value = value;
    }
    public UpdateWalletPacket() {
        this(null, 0);
    }

    public String getUuid() {
        return uuid;
    }

    public float getValue() {
        return value;
    }
}
