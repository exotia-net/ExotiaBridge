package net.exotia.bridge.shared;

public class Endpoints {
    public static final String AUTH_HEADER = "ExotiaKey";
    public static final String AUTH_ME = "/auth/me";
    public static final String AUTH_SIGNUP = "/auth/signUp";
    public static final String PLAYER_BALANCE = "/api/servers/%s";
    public static final String PLAYER_WALLET_BALANCE = "/api/wallet";

    public static final String GET_PLAYER_BALANCE_WS = "GET /servers/%s/economy %s";
    public static final String UPDATE_PLAYER_BALANCE_WS = "POST /servers/%s/economy %s %s";

    public static final String GET_PLAYER_CALENDAR = "GET /calendars %s";
    public static final String UPDATE_PLAYER_CALENDAR = "PUT /calendars %s %s %s";

    public static final String WEBSOCKET = "/ws";

    public static String getUri(String endpoint, ApiConfiguration configuration) {
        return String.join("", configuration.getBaseUrl(), String.format(endpoint, configuration.getServerId()));
    }
}
