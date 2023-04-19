package net.exotia.bridge.plugin.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class User {
    private UUID uniqueId;
    private String firstIp;
    private String lastIp;
}
