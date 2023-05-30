package net.exotia.bridge.proxy.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import net.exotia.bridge.shared.ApiConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PluginConfiguration extends OkaeriConfig implements ApiConfiguration {
    private String serverId = "proxy";
    private String baseUrl = "https://api.exotia.net";
    private String apiKey = "d!noT41*Z8UbxB}JZ<s8'#'GA";

    private List<String> apiErrorMessage = Arrays.asList("<red>Wystąpił błąd podczas komunikacji z serwerem API!</red>", "<red>{message}</red>");
    private List<String> userCreatedMessage = Arrays.asList("<green>Konto zostało pomyślnie utworzone</green>", "<green>Dołącz ponownie na serwer!</green>");

    public String getApiErrorMessage(String message) {
        return this.apiErrorMessage.stream()
                .map(line -> line.replace("{message}", message))
                .collect(Collectors.joining("\n"));
    }
    public String getUserCreatedMessage() {
        return String.join("\n", this.userCreatedMessage);
    }

    @Override
    public boolean isProxyServer() {
        return true;
    }

    @Override
    public boolean websocketAutoReconnect() {
        return true;
    }
}