package net.exotia.bridge.shared.services.entities;

import lombok.Builder;
import net.exotia.bridge.api.user.ApiUser;

import java.util.UUID;

@Builder
public class User implements ApiUser {
    private UUID uniqueId;
    private String nickname;
    private String firstIp;
    private String lastIp;

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getFirstIp() {
        return this.firstIp;
    }

    @Override
    public String getLastIp() {
        return this.lastIp;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }
}
