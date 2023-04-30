package net.exotia.bridge.shared.services.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.exotia.bridge.shared.ApiConfiguration;

import java.util.UUID;

import static net.exotia.bridge.shared.utils.CipherUtil.encrypt;
import static net.exotia.bridge.shared.utils.CipherUtil.sha256;

@Getter
@Builder
@AllArgsConstructor
public class ExotiaPlayer {
    private UUID uniqueId;
    private String username;
    private String ip;

    public String getUniqueIdString() {
        return this.uniqueId.toString();
    }

    @SneakyThrows
    public String getCipher(ApiConfiguration configuration) {
        byte[] key = sha256(configuration.getApiKey());
        return encrypt(String.join("|", this.getUniqueIdString(), this.getIp(), this.getUsername()), key);
    }
}
