package net.exotia.bridge.shared.services.responses;

import java.util.UUID;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFirstIp() {
        return firstIp;
    }

    public int getId() {
        return id;
    }

    public String getLastIp() {
        return lastIp;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
