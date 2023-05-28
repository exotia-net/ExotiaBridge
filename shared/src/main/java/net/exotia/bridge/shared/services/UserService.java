package net.exotia.bridge.shared.services;

import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.api.user.ApiUserService;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.messaging_api.packets.UserNeedUpdatePacket;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.Bridge;
import net.exotia.bridge.shared.http.HttpResponse;
import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.http.exceptions.HttpRequestException;
import net.exotia.bridge.shared.messaging.PluginMessagingService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.services.responses.EconomyResponse;
import net.exotia.bridge.shared.services.responses.UserResponse;
import net.exotia.bridge.shared.services.responses.WalletResponse;
import net.exotia.bridge.shared.websocket.SocketService;
import okhttp3.Response;
import okhttp3.WebSocketListener;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.exotia.bridge.shared.Endpoints.*;

public class UserService extends SocketService implements ApiUserService {
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

    public void setupSocket(WebSocketListener webSocketListener) {
        this.onConstruct(this.httpService.prepareWebSocketConnection(this.configuration, webSocketListener), this.configuration);
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
//        this.httpService.post(getUri(AUTH_SIGNUP, this.configuration), null, (o, result) -> {
//            Response response = result.getResponse();
//            function.accept(response.code() == 200 || response.code() == 201, result.getResponseString());
//        }, Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
//        this.bridge.runAsync(() -> {
//            this.httpService.post(getUri(AUTH_SIGNUP, this.configuration), null, (o, result) -> {
//                Response response = result.getResponse();
//                function.accept(response.code() == 200 || response.code() == 201, result.getResponseString());
//            }, Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
//        });
    }
    public CompletableFuture<Boolean> signUp(ExotiaPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            HttpResponse<?> httpResponse = this.httpService.post(getUri(AUTH_SIGNUP, this.configuration),
                    null, Map.of(AUTH_HEADER, player.getCipher(this.configuration))
            );
            Response response = httpResponse.getResponse();
            if (!List.of(201, 200).contains(response.code()))
                throw new HttpRequestException("Failed with status code " + response.code());
           return true;
        });
    }
    public CompletableFuture<User> authorize(ExotiaPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            HttpResponse<UserResponse> httpResponse = this.httpService.get(
                    getUri(AUTH_ME, this.configuration),UserResponse.class,
                    Map.of(AUTH_HEADER, player.getCipher(this.configuration))
            );
            Response response = httpResponse.getResponse();
            if (!List.of(200, 401).contains(response.code()))
                throw new HttpRequestException("Failed with status code " + response.code());

            User user = null;
            if (httpResponse.get() != null) {
                user = httpResponse.get().getUser(player.getUsername());
                this.registerUser(user);
            }
            return user;
        });
    }
    public CompletableFuture<Integer> getPlayerBalance(ExotiaPlayer exotiaPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String uri = getUri(PLAYER_BALANCE, this.configuration);
                HttpResponse<EconomyResponse> response = this.httpService.get(uri, EconomyResponse.class, Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration)));
                return response.get().getBalance();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw exception;
            }
        });
    }
    public CompletableFuture<WalletResponse> getWallet(ExotiaPlayer exotiaPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.httpService.get(
                        getUri(PLAYER_WALLET_BALANCE, this.configuration),WalletResponse.class,
                        Map.of(AUTH_HEADER, exotiaPlayer.getCipher(this.configuration))
                ).get();
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
        this.setBalance(apiUser.getUniqueId(), apiUser.getBalance());
    }
}
