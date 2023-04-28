package net.exotia.bridge.api.user;

import java.util.Set;
import java.util.UUID;

public interface ApiUserService {
    ApiUser getUser(UUID uniqueId);
    Set<ApiUser> getUsers();
    String getUserCipher(UUID uniqueId, String username, String ip);
}
