package net.exotia.bridge.shared.services;

import lombok.SneakyThrows;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.Callback;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.exotia.bridge.shared.Endpoints.AUTH_ME;
import static net.exotia.bridge.shared.Endpoints.AUTH_SIGNUP;
import static net.exotia.bridge.shared.utils.CipherUtil.encrypt;
import static net.exotia.bridge.shared.utils.CipherUtil.sha256;

public class UserService {
    private HttpService httpService;
    private ApiConfiguration configuration;
    private Bridge bridge;

    public UserService(ApiConfiguration apiConfiguration, Bridge bridge) {
        this.httpService = new HttpService();
        this.bridge = bridge;
        this.configuration = apiConfiguration;
    }

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

    public void isAuthorized(UUID uniqueId, String username, Callback callback) {
        this.bridge.async(() -> {
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
            }), Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
            callback.onSuccess(atomicBoolean.get());
        });
//        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
//            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
//            this.httpService.get(AUTH_ME, UserResponse.class, ((userResponse, response) -> {
//                if (response.code() == 200) {
//                    this.registerUser(User.builder()
//                            .uniqueId(userResponse.getUuid())
//                            .firstIp(userResponse.getFirstIp())
//                            .lastIp(userResponse.getLastIp())
//                            .build());
//                    atomicBoolean.set(true);
//                }
//            }), Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
//            callback.onSuccess(atomicBoolean.get());
//        });
    }
    public void signUp(UUID uniqueId, String username, Callback callback) {
        this.bridge.async(() -> {
            this.httpService.post(AUTH_SIGNUP, null, null, (o, response) -> {
                System.out.println(response.code());
                callback.onSuccess(response.code() == 200 || response.code() == 201);
            }, Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
        });
//        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
//            this.httpService.post(AUTH_SIGNUP, null, null, (o, response) -> {
//                System.out.println(response.code());
//                callback.onSuccess(response.code() == 200 || response.code() == 201);
//            }, Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
//        });
    }

    public void save(User user) {

    }

    @SneakyThrows
    private String getUserCipher(UUID uniqueId, String username) {
        byte[] key = sha256(this.configuration.getApiKey());
        return encrypt(String.join("|", uniqueId.toString(), "0.0.0.0", username), key);
    }
}
