package net.exotia.bridge.shared.websocket;

import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.http.HttpService;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.util.UUID;

import static net.exotia.bridge.shared.Endpoints.GET_PLAYER_BALANCE_WS;
import static net.exotia.bridge.shared.Endpoints.UPDATE_PLAYER_BALANCE_WS;

public class SocketService {
    private WebSocket webSocket;
    private HttpService httpService;
    private ApiConfiguration configuration;
    private WebSocketListener webSocketListener;
    private String serverId;

    public void onConstruct(WebSocketListener webSocketListener, HttpService httpService, ApiConfiguration apiConfiguration) {
        this.httpService = httpService;
        this.configuration = apiConfiguration;
        this.webSocketListener = webSocketListener;
        this.serverId = apiConfiguration.getServerId();
        this.reconnect();
    }

    public void sendMessage(String message) {
        this.webSocket.send(message);
    }
    public void requestBalance(UUID uuid) {
        this.webSocket.send(String.format(GET_PLAYER_BALANCE_WS, this.serverId, uuid.toString()));
    }
    public void setBalance(UUID uuid, int balance) {
        System.out.println(String.format(UPDATE_PLAYER_BALANCE_WS, this.serverId, uuid.toString(), balance));
        this.webSocket.send(String.format(UPDATE_PLAYER_BALANCE_WS, this.serverId, uuid.toString(), balance));
    }

    public void reconnect() {
        if (this.webSocket != null) this.webSocket.cancel();
        this.webSocket = this.httpService.prepareWebSocketConnection(this.configuration, this.webSocketListener);
    }
}
