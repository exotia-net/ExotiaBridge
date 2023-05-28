package net.exotia.bridge.shared.websocket;

import lombok.Getter;
import java.util.UUID;

@Getter
public class SocketResponse {
    private int code;
    private String message;
    private String data;
    private String endpoint;
    private String uuid;

    public UUID getUuid() {
        return UUID.fromString(this.uuid);
    }
}
