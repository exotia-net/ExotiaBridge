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
    private final HttpService httpService;
    private final ApiConfiguration configuration;
    private final Set<User> users = new HashSet<>();

    public UserService(ApiConfiguration apiConfiguration, Bridge bridge) {
        this.httpService = bridge.getHttpService();
        this.configuration = apiConfiguration;
    }

    public void setupSocket(WebSocketListener webSocketListener) {
        this.onConstruct(this.httpService.prepareWebSocketConnection(this.configuration, webSocketListener), this.configuration);
    }

    public void registerUser(User user) {
        if (this.getUser(user.getUniqueId()) != null) return;
        this.users.add(user);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.stream().filter(user -> user.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }
    @Override
    public Set<ApiUser> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }
    @Override
    public void saveBalance(ApiUser apiUser) {
        this.setBalance(apiUser.getUniqueId(), apiUser.getBalance());
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
}
