package net.exotia.bridge.shared.websocket;

import net.exotia.bridge.shared.ApiConfiguration;
import okhttp3.WebSocket;

import java.util.UUID;

import static net.exotia.bridge.shared.Endpoints.GET_PLAYER_BALANCE_WS;
import static net.exotia.bridge.shared.Endpoints.UPDATE_PLAYER_BALANCE_WS;

public class SocketService {
    private WebSocket webSocket;
    private String serverId;

    public void onConstruct(WebSocket webSocket, ApiConfiguration apiConfiguration) {
        this.webSocket = webSocket;
        this.serverId = apiConfiguration.getServerId();
    }

    public void sendMessage(String message) {
        this.webSocket.send(message);
    }

    public void requestBalance(UUID uuid) {
        this.webSocket.send(String.format(GET_PLAYER_BALANCE_WS, this.serverId, uuid.toString()));
    }
    public void setBalance(UUID uuid, int balance) {
        this.webSocket.send(String.format(UPDATE_PLAYER_BALANCE_WS, this.serverId, uuid.toString(), balance));
    }
}
