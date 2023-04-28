package net.exotia.bridge.plugin.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;
import net.exotia.bridge.shared.ApiConfiguration;

@Getter
public class PluginConfiguration extends OkaeriConfig implements ApiConfiguration {
    private String serverId = "survival";
    @Comment("Interface url.")
    private String baseUrl = "https://api.exotia.net";
    private String apiKey = "d!noT41*Z8UbxB}JZ<s8'#'GA";
}
