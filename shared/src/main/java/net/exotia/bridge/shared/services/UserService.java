package net.exotia.bridge.shared.services;

import lombok.SneakyThrows;
import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.UserResponse;
import okhttp3.Response;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.exotia.bridge.shared.Endpoints.*;
import static net.exotia.bridge.shared.utils.CipherUtil.encrypt;
import static net.exotia.bridge.shared.utils.CipherUtil.sha256;

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

    public void isAuthorized(UUID uniqueId, String username, String ip, BiConsumer<Boolean, String> function) {
        this.bridge.runAsync(() -> {
            this.httpService.get(getUri(AUTH_ME, this.configuration), UserResponse.class, ((userResponse, result) -> {
                if (result.getResponse().code() == 200) {
                    this.registerUser(User.builder()
                            .uuid(userResponse.getUuid())
                            .nickname(username)
                            .firstIp(userResponse.getFirstIp())
                            .lastIp(userResponse.getLastIp())
                            .build());
                }
                function.accept(result.getResponse().code() == 200, result.getResponseString());
            }), Map.of(AUTH_HEADER, this.getUserCipher(uniqueId, username, ip)));
        });
    }
    public void signUp(UUID uniqueId, String username, String ip, BiConsumer<Boolean, String> function) {
        this.bridge.runAsync(() -> {
            this.httpService.post(getUri(AUTH_SIGNUP, this.configuration), null, (o, result) -> {
                Response response = result.getResponse();
                function.accept(response.code() == 200 || response.code() == 201, result.getResponseString());
            }, Map.of(AUTH_HEADER, this.getUserCipher(uniqueId, username, ip)));
        });
    }

    public CompletableFuture<Set<User>> getUsersToUpdate() {
        return CompletableFuture.supplyAsync(() -> this.users.stream()
                .filter(user -> user.getUpdate().isUpdatable())
                .collect(Collectors.toSet()));
    }

    public void save(User user) {

    }

    @Override
    @SneakyThrows
    public String getUserCipher(UUID uniqueId, String username, String ip) {
        byte[] key = sha256(this.configuration.getApiKey());
        return encrypt(String.join("|", uniqueId.toString(), ip, username), key);
    }
}
