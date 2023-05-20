package net.exotia.bridge.shared.services.responses;

import net.exotia.bridge.shared.services.entities.User;

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

    public User getUser(String nickname) {
        return User.builder()
                .uuid(UUID.fromString(this.uuid))
                .nickname(nickname)
                .firstIp(this.firstIp)
                .lastIp(this.lastIp)
                .build();
    }
}
