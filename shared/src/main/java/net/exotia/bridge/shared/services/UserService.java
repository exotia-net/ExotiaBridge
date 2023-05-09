package net.exotia.bridge.shared.services;

import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.messaging_api.packets.UserNeedUpdatePacket;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.messaging.PluginMessagingService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.EconomyResponse;
import net.exotia.bridge.shared.services.responses.UserResponse;
import okhttp3.Response;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.exotia.bridge.shared.Endpoints.*;

public class UserService implements ApiUserService {
    private HttpService httpService;
    private ApiConfiguration configuration;
    private Bridge bridge;
    private PluginMessagingService pluginMessagingService;

    public UserService(ApiConfiguration apiConfiguration, Bridge bridge, PluginMessagingService pluginMessagingService) {
        this.httpService = bridge.getHttpService();
        this.bridge = bridge;
        this.configuration = apiConfiguration;
        this.pluginMessagingService = pluginMessagingService;
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
        return CompletableFuture.supplyAsync(() -> {
            try {
                EconomyResponse response = this.httpService.get(getUri(GET_PLAYER_BALANCE, this.configuration), EconomyResponse.class, Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
                return response.getBalance();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw exception;
            }
        });
    }

    public CompletableFuture<Set<User>> getUsersToUpdate() {
        return CompletableFuture.supplyAsync(() -> this.users.stream()
                .filter(user -> user.getUpdate().isUpdatable())
                .collect(Collectors.toSet()));
    }

    @Override
    public void saveBalance(ApiUser apiUser) {
        this.pluginMessagingService.sendMessageData(
                this.pluginMessagingService.getPlayer(apiUser.getUniqueId()),
                MessagingChannels.USER_NEED_UPDATE,
                new UserNeedUpdatePacket(this.configuration.getServerId(), apiUser.getUniqueId().toString(), apiUser.getBalance())
        );
    }

    public void sendUpdateNotify() {}
}
