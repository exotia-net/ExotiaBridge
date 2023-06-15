package net.exotia.bridge.shared.services.responses;

import lombok.Getter;
import net.exotia.bridge.shared.services.entities.User;

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

    public User getUser(String nickname) {
        return User.builder()
                .uuid(UUID.fromString(this.uuid))
                .nickname(nickname)
                .firstIp(this.firstIp)
                .lastIp(this.lastIp)
                .build();
    }
}
