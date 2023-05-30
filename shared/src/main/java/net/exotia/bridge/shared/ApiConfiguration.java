package net.exotia.bridge.shared;

public interface ApiConfiguration {
    String getServerId();
    String getBaseUrl();
    String getApiKey();
    boolean isProxyServer();
    boolean websocketAutoReconnect();
}
