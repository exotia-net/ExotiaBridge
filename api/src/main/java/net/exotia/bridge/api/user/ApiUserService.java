package net.exotia.bridge.api.user;

import java.util.List;
import java.util.UUID;

public interface ApiUserService {
    ApiUser getUser(UUID uniqueId);
    String getUserCipher(UUID uniqueId, String username);
}
