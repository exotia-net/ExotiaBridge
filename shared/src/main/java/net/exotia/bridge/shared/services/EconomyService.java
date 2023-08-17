package net.exotia.bridge.shared.services;

import com.google.gson.Gson;
import net.exotia.bridge.api.entities.TopUserEntity;
import net.exotia.bridge.api.user.ApiEconomyService;
import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.http.HttpResponse;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.http.exceptions.HttpRequestException;
import net.exotia.bridge.shared.services.entities.User;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.exotia.bridge.shared.Endpoints.*;

public class EconomyService implements ApiEconomyService {
    private final UserService userService;
    private final HttpService httpService;
    private final ApiConfiguration configuration;
    private final Gson gson = new Gson();

    public EconomyService(UserService userService, HttpService httpService, ApiConfiguration configuration) {
        this.userService = userService;
        this.configuration = configuration;
        this.httpService = httpService;
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
    public CompletableFuture<List<TopUserEntity>> getTops(int limit) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = this.httpService.get(
                    getUri(ECONOMY_TOP, this.configuration, "?limit="+limit), Map.of(AUTH_HEADER, this.httpService.getApiKey(this.configuration)));
            if (!List.of(201, 200).contains(response.code()))
                throw new HttpRequestException("Failed with status code " + response.code());

            try {
                assert response.body() != null;
                return List.of(this.gson.fromJson(response.body().string(), TopUserEntity.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void save(ApiUser apiUser) {
        this.userService.setBalance(apiUser.getUniqueId(), apiUser.getBalance());
    }
}
