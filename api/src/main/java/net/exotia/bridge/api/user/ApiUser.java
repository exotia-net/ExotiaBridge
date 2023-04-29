package net.exotia.bridge.api.user;

import java.util.UUID;

public interface ApiUser {
    UUID getUniqueId();
    String getFirstIp();
    String getLastIp();
    String getNickname();
}
