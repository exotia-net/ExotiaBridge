package net.exotia.bridge.plugin.user;

import eu.okaeri.injector.annotation.Inject;
import lombok.SneakyThrows;
import net.exotia.bridge.plugin.Endpoints;
import net.exotia.bridge.plugin.api.requests.CreateUserRequest;
import net.exotia.bridge.plugin.api.responses.UserResponse;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.http.Callback;
import net.exotia.bridge.plugin.http.HttpService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.exotia.bridge.plugin.Endpoints.AUTH_ME;
import static net.exotia.bridge.plugin.Endpoints.AUTH_SIGNUP;
import static net.exotia.bridge.plugin.utils.CipherUtil.*;

public class UserService {
    @Inject private HttpService httpService;
    @Inject private PluginConfiguration configuration;
    @Inject private Plugin plugin;
    private final List<User> users = new ArrayList<>();

    public void registerUser(User user) {
        if (this.getUser(user.getUniqueId()) != null) return;
        this.users.add(user);
    }

    public User getUser(UUID uniqueId) {
        return this.users.stream()
                .filter(user -> user.getUniqueId() == uniqueId)
                .findFirst().orElse(null);
    }

    public void isAuthorized(Player player, Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            this.httpService.get(AUTH_ME, UserResponse.class, ((userResponse, response) -> {
                if (response.code() == 200) {
                    this.registerUser(User.builder()
                            .uniqueId(userResponse.getUuid())
                            .firstIp(userResponse.getFirstIp())
                            .lastIp(userResponse.getLastIp())
                            .build());
                    atomicBoolean.set(true);
                }
            }), Map.of("ExotiaKey", this.getUserCipher(player)));
            callback.onSuccess(atomicBoolean.get());
        });
    }
    public void signUp(Player player, Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            this.httpService.post(AUTH_SIGNUP, new CreateUserRequest(player.getUniqueId().toString(), "0.0.0.0"), null, (o, response) -> {
                System.out.println(response.code());
                callback.onSuccess(response.code() == 200 || response.code() == 201);
            }, null);
        });
    }

    public void save(User user) {

    }

    @SneakyThrows
    private String getUserCipher(Player player) {
        byte[] key = sha256(this.configuration.getApiKey());
        return encrypt(String.join("|", player.getUniqueId().toString(), "0.0.0.0", player.getName()), key);
    }
}
