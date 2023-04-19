package net.exotia.bridge.plugin.user;

import eu.okaeri.injector.annotation.Inject;
import lombok.SneakyThrows;
import net.exotia.bridge.plugin.api.requests.CreateUserRequest;
import net.exotia.bridge.plugin.api.responses.UserResponse;
import net.exotia.bridge.plugin.configuration.PluginConfiguration;
import net.exotia.bridge.plugin.http.Callback;
import net.exotia.bridge.plugin.http.HttpService;
import net.exotia.bridge.plugin.utils.CipherUtil;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public void isAuthorized(Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            this.httpService.get("https://api.exotia.net/auth/me", UserResponse.class, ((userResponse, response) -> {
                if (response.code() == 200) {
                    this.registerUser(User.builder()
                            .uniqueId(userResponse.getUuid())
                            .firstIp(userResponse.getFirstIp())
                            .lastIp(userResponse.getLastIp())
                            .build());
                    atomicBoolean.set(true);
                }
            }));
            callback.onSuccess(atomicBoolean.get());
        });
    }

    public void signUp(Player player, Callback callback) {
        this.httpService.post("https://api.exotia.net/auth/signUp", new CreateUserRequest(player.getUniqueId().toString(), "0.0.0.0"), null, (o, response) -> {
            if (response.code() != 201) {
                callback.onSuccess(false);
                try {
                    player.kickPlayer(response.code() + " " + response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            callback.onSuccess(true);
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
