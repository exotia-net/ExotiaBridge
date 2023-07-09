package net.exotia.bridge.shared.services;

import net.exotia.bridge.api.user.ApiEconomyService;
import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.shared.services.entities.User;

import java.util.UUID;

public class EconomyService implements ApiEconomyService {
    private final UserService userService;

    public EconomyService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean has(ApiUser apiUser, int value) {
        return apiUser.getBalance() >= value;
    }

    @Override
    public boolean has(UUID uuid, int value) {
        User user = this.userService.getUser(uuid);
        return this.has(user, value);
    }

    @Override
    public void take(ApiUser apiUser, int value) {
        apiUser.setBalance(apiUser.getBalance()-value);
    }

    @Override
    public void take(UUID uuid, int value) {
        User user = this.userService.getUser(uuid);
        this.take(user, value);
    }

    @Override
    public void give(ApiUser apiUser, int value) {
        apiUser.setBalance(apiUser.getBalance()+value);
    }

    @Override
    public void give(UUID uuid, int value) {
        User user = this.userService.getUser(uuid);
        this.give(user, value);
    }

    @Override
    public void giveRaw(UUID uuid, int value) {
        this.userService.sendRawSocketMessage(String.format("POST /servers/{serverId}/economy/add %s %s", uuid, value));
    }

    @Override
    public void save(UUID uuid) {
        User user = this.userService.getUser(uuid);
        this.save(user);
    }
    @Override
    public void save(ApiUser apiUser) {
        this.userService.setBalance(apiUser.getUniqueId(), apiUser.getBalance());
    }
}
