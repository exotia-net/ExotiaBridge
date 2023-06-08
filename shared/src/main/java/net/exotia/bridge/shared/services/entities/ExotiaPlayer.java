package net.exotia.bridge.shared.services.entities;

import lombok.Getter;
import lombok.SneakyThrows;
import net.exotia.bridge.shared.ApiConfiguration;

import java.util.UUID;

import static net.exotia.bridge.shared.utils.CipherUtil.encrypt;
import static net.exotia.bridge.shared.utils.CipherUtil.sha256;

@Getter
public class ExotiaPlayer {
    private final UUID uniqueId;
    private final String username;
    private final String ip;

    public ExotiaPlayer(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.ip = "0.0.0.0";
    }
    public ExotiaPlayer(UUID uniqueId, String username, String lastIp) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.ip = lastIp;
    }

    public String getUniqueIdString() {
        return this.uniqueId.toString();
    }

    @SneakyThrows
    public String getCipher(ApiConfiguration configuration) {
        byte[] key = sha256(configuration.getApiKey());
        return encrypt(String.join("|", this.getUniqueIdString(), this.ip, this.username), key);
    }
}
