package net.exotia.bridge.api.user;

import net.exotia.bridge.api.entities.TopUserEntity;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ApiEconomyService {
    boolean has(ApiUser apiUser, int value);
    boolean has(UUID uuid, int value);
    void take(ApiUser apiUser, int value);
    void take(UUID uuid, int value);
    void give(ApiUser apiUser, int value);
    void give(UUID uuid, int value);
    void giveRaw(UUID uuid, int value);
    void save(ApiUser apiUser);
    void save(UUID uuid);

    CompletableFuture<List<TopUserEntity>> getTops(int limit);
}
