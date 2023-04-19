package net.exotia.bridge.plugin.api.responses;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponse {
    private String createdAt;
    private String firstIp;
    private int id;
    private String lastIp;
    private String updatedAt;
    private String uuid;

    public UUID getUuid() {
        return UUID.fromString(this.uuid);
    }
}
