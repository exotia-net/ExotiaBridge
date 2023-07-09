package net.exotia.bridge.shared.websocket;

import net.exotia.bridge.api.entities.CalendarUser;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.http.HttpService;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.util.UUID;
import java.util.stream.Collectors;

import static net.exotia.bridge.shared.Endpoints.*;

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
        System.out.println(message);
        this.webSocket.send(message.replace("{serverId}", this.serverId));
    }
    public void requestBalance(UUID uuid) {
        this.sendMessage(String.format(GET_PLAYER_BALANCE_WS, this.serverId, uuid.toString()));
    }
    public void setBalance(UUID uuid, int balance) {
        this.sendMessage(String.format(UPDATE_PLAYER_BALANCE_WS, this.serverId, uuid, balance));
    }
    public void requestCalendar(UUID uuid) {
        this.sendMessage(String.format(GET_PLAYER_CALENDAR, uuid.toString()));
    }
    public void sendCalendarRequest(UUID uuid, CalendarUser calendarUser) {
        String obtained = calendarUser.getNotObtainedRewards().stream().map(String::valueOf).collect(Collectors.joining("|"));
        this.sendMessage(String.format(UPDATE_PLAYER_CALENDAR, uuid, calendarUser.getStep(), calendarUser.getStreakDays(), obtained, calendarUser.getLastObtained()));
    }

    public void reconnect() {
        if (this.webSocket != null) this.webSocket.cancel();
        this.webSocket = this.httpService.prepareWebSocketConnection(this.configuration, this.webSocketListener);
    }
}
