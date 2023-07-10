package net.exotia.bridge.spigot.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;
import net.exotia.bridge.shared.ApiConfiguration;

import java.util.List;

@Getter
public class PluginConfiguration extends OkaeriConfig implements ApiConfiguration {
    private String serverId = "Survival";
    @Comment("Interface url.")
    private String baseUrl = "https://api.exotia.net";
    private String apiKey = "d!noT41*Z8UbxB}JZ<s8'#'GA";
    private boolean enableAddressChecker = false;
    private List<String> addresses = List.of("wings-s1-ext.exotia.net:50001");
    private String authFailedMessage = "<red>Nie mozna autoryzowac polaczenia z adresu {hostname}";

    @Override
    public boolean isProxyServer() {
        return false;
    }

    @Override
    public boolean websocketAutoReconnect() {
        return true;
    }
}
