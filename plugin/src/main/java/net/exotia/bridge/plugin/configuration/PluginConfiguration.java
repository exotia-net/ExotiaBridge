package net.exotia.bridge.plugin.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;

@Getter
public class PluginConfiguration extends OkaeriConfig {
    private String serverId = "survival";
    @Comment("Interface url.")
    private String baseUrl = "https://api.exotia.net";
    private String apiKey = "XXXX";
}
