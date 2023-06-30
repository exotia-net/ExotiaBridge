package net.exotia.bridge.api.user;

import java.util.Set;
import java.util.UUID;

public interface ApiUserService {
    ApiUser getUser(UUID uniqueId);
    Set<ApiUser> getUsers();
    void saveBalance(ApiUser apiUser);
    void saveCalendar(ApiUser apiUser);
    void sendRawSocketMessage(String message);
}
