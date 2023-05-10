package net.exotia.bridge.shared.services.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.exotia.bridge.api.user.ApiUser;

import java.util.UUID;

@Getter
@Setter
@Builder
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

    public ExotiaPlayer getExotiaPlayer() {
        return new ExotiaPlayer(this.uuid, this.nickname, this.lastIp);
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }
}
