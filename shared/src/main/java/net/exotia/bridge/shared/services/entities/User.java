package net.exotia.bridge.shared.services.entities;

import lombok.Builder;
import lombok.Setter;
import net.exotia.bridge.api.user.ApiUser;

import java.util.UUID;

@Builder
@Setter
public class User implements ApiUser {
    private UUID uuid;
    private String nickname;
    private String firstIp;
    private String lastIp;
    private Update update;

    /**
     * Player balance (This value is different on every server)
     */
    private int balance;

    @Override
    public UUID getUniqueId() {
        return this.uuid;
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
    public Update getUpdate() {
        return this.update;
    }

    public ExotiaPlayer getExotiaPlayer() {
        return ExotiaPlayer.builder()
                .uniqueId(this.uuid)
                .username(this.nickname)
                .ip(this.lastIp)
                .build();
    }
}
