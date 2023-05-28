package net.exotia.bridge.spigot.client;

import com.google.gson.Gson;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.User;
import net.exotia.bridge.shared.websocket.SocketResponse;
import net.exotia.bridge.spigot.configuration.PluginConfiguration;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class WebSocketClient extends WebSocketListener {
    @Inject private Logger logger;
    @Inject private UserService userService;
    @Inject private PluginConfiguration configuration;

    private final Gson gson = new Gson();

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        this.logger.info("Connected to websocket server!");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        this.logger.info("Received a message!");
        SocketResponse socketResponse = this.gson.fromJson(text, SocketResponse.class);
        User user = null;
        if (socketResponse.getUuid() != null) {
            user = this.userService.getUser(socketResponse.getUuid());
        }

        String endpoint = socketResponse.getEndpoint().replace(this.configuration.getServerId(), "{serverId}");

        switch (endpoint) {
            case "/servers/{serverId}/economy":
                assert user != null && socketResponse.getData() != null;
                user.setBalance(Integer.parseInt(socketResponse.getData()));
                break;
            default:
                this.logger.severe(String.format("Invalid request! %s (%s)", socketResponse.getEndpoint(), socketResponse.getMessage()));
        }
        this.logger.info(text);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        this.logger.info("WebSocket connection closed!");
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        this.logger.severe("WebSocket failure!");
        this.logger.info(t.getMessage());
    }
}
