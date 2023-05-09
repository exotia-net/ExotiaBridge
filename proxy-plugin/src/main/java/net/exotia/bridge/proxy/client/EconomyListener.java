package net.exotia.bridge.proxy.client;

import eu.okaeri.injector.annotation.Inject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class EconomyListener extends WebSocketListener {
    @Inject private Logger logger;

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        this.logger.info("Connected to websocket server!");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        this.logger.info("Received a message!");
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
