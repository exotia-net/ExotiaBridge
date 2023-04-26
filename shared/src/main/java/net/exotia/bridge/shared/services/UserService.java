package net.exotia.bridge.shared.services;

import lombok.SneakyThrows;
import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.Callback;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.UserResponse;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

    //private final HashMap<UUID, User> users = new HashMap<>();
    private List<User> users = new ArrayList<>();

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

    public List<User> getUsers() {
        return this.users;
    }
    public void isAuthorized(UUID uniqueId, String username, Callback callback) {
        this.bridge.async(() -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            AtomicReference<User> userAtomicReference = new AtomicReference<User>(null);
            this.httpService.get(getUri(AUTH_ME, this.configuration), UserResponse.class, ((userResponse, result) -> {
                if (result.getResponse().code() == 200) {
                    userAtomicReference.set(User.builder()
                            .uuid(userResponse.getUuid())
                            .nickname(username)
                            .firstIp(userResponse.getFirstIp())
                            .lastIp(userResponse.getLastIp())
                            .build());
                    atomicBoolean.set(true);
                }
            }), Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
            callback.onSuccess(atomicBoolean.get(), userAtomicReference.get());
        });
    }

    public void signUp(UUID uniqueId, String username, Callback callback) {
        this.bridge.async(() -> {
            this.httpService.post(getUri(AUTH_SIGNUP, this.configuration), null, (o, result) -> {
                Response response = result.getResponse();
                callback.onSuccess(response.code() == 200 || response.code() == 201, result.getResponseString());
            }, Map.of("ExotiaKey", this.getUserCipher(uniqueId, username)));
        });
        System.out.println(this.getUserCipher(uniqueId, username));
    }

    public void save(User user) {

    }

    private String getResponseBody(Response response) {
        try {
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            return responseBodyCopy.string();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    @SneakyThrows
    public String getUserCipher(UUID uniqueId, String username) {
        byte[] key = sha256(this.configuration.getApiKey());
        return encrypt(String.join("|", uniqueId.toString(), "0.0.0.0", username), key);
    }
}
