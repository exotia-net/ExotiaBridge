package net.exotia.bridge.shared.services;

import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.exceptions.ServerIdIsInvalidException;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.EconomyResponse;
import net.exotia.bridge.shared.services.responses.UserResponse;
import okhttp3.Response;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.exotia.bridge.shared.Endpoints.*;

public class UserService implements ApiUserService {
    private HttpService httpService;
    private ApiConfiguration configuration;
    private Bridge bridge;

    public UserService(ApiConfiguration apiConfiguration, Bridge bridge) {
        this.httpService = bridge.getHttpService();
        this.bridge = bridge;
        this.configuration = apiConfiguration;
    }

    private Set<User> users = new HashSet<>();

    public void registerUser(User user) {
        if (this.getUser(user.getUniqueId()) != null) return;
        this.users.add(user);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.stream()
                .filter(user -> user.getUniqueId().equals(uniqueId))
                .findFirst().orElse(null);
    }

    @Override
    public Set<ApiUser> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }

    public void isAuthorized(ExotiaPlayer exotiaPlayer, BiConsumer<Boolean, User> function) {
        this.bridge.runAsync(() -> {
            this.httpService.get(getUri(AUTH_ME, this.configuration), UserResponse.class, ((userResponse, result) -> {
                User user = null;
                if (result.getResponse().code() == 200) {
                    user = User.builder()
                            .uuid(userResponse.getUuid())
                            .nickname(exotiaPlayer.getUsername())
                            .firstIp(userResponse.getFirstIp())
                            .lastIp(userResponse.getLastIp())
                            .build();
                    this.registerUser(user);
                }
                function.accept(result.getResponse().code() == 200, user);
            }), Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
        });
    }
    public void signUp(ExotiaPlayer exotiaPlayer, BiConsumer<Boolean, String> function) {
        this.bridge.runAsync(() -> {
            this.httpService.post(getUri(AUTH_SIGNUP, this.configuration), null, (o, result) -> {
                Response response = result.getResponse();
                function.accept(response.code() == 200 || response.code() == 201, result.getResponseString());
            }, Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
        });
    }
    public CompletableFuture<Integer> getPlayerBalance(ExotiaPlayer exotiaPlayer) {
        System.out.println("=================[ Request1 ]===============");
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("=================[ Request2 ]===============");
            AtomicInteger atomicInteger = new AtomicInteger(0);
            this.httpService.get(getUri(GET_PLAYER_BALANCE, this.configuration), EconomyResponse.class, ((economyResponse, result) -> {
                Response response = result.getResponse();
                System.out.println("=================[ Request3 ]===============");
                System.out.println(response.code());
                if (response.code() != 200) throw new ServerIdIsInvalidException(this.configuration.getServerId());
                atomicInteger.set(economyResponse.getBalance());
            }), Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
            System.out.println("================================");
           return atomicInteger.get();
        });
    }

    public CompletableFuture<Set<User>> getUsersToUpdate() {
        return CompletableFuture.supplyAsync(() -> this.users.stream()
                .filter(user -> user.getUpdate().isUpdatable())
                .collect(Collectors.toSet()));
    }

    public void save(User user) {

    }
}
