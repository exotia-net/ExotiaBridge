package net.exotia.bridge.messaging_api.packets;

import net.exotia.bridge.messaging_api.MessagingPacket;

public class TokenPacket extends MessagingPacket {
    private final String token;
    private final String uuid;

    public TokenPacket(String token, String uuid) {
        this.token = token;
        this.uuid = uuid;
    }
    public TokenPacket() {
        this(null, null);
    }

    public String getToken() {
        return this.token;
    }
    public String getUuid() {
        return this.uuid;
    }
}
