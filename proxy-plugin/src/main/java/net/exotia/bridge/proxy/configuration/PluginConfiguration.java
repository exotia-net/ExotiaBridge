package net.exotia.bridge.proxy.configuration;

import lombok.Getter;
import net.exotia.bridge.shared.ApiConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PluginConfiguration implements ApiConfiguration {
    private String serverId = "proxy";
    private String baseUrl = "https://api.exotia.net";
    private String apiKey = "d!noT41*Z8UbxB}JZ<s8'#'GA";

    private List<String> apiErrorMessage = List.of("<red>Wystąpił błąd podczas komunikacji z serwerem API!</red>", "<red>({response_code}) {message}</red>");
    private List<String> userCreatedMessage = List.of("<green>Konto zostało pomyślnie utworzone</green>", "<green>Dołącz ponownie na serwer!</green>");
    public String getFormattedError(int code, String message) {
        return this.apiErrorMessage.stream()
                .map(line -> line.replace("{response_code}", String.valueOf(code)).replace("{message}", message))
                .collect(Collectors.joining("\n"));
    }
    public String getFormattedUserCreated() {
        return String.join("\n", this.userCreatedMessage);
    }
}

