package net.exotia.bridge.shared.factory;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ConfigurationFactory {
    private final File dataDirectory;
    private final Configurer configurer;
    private OkaeriSerdesPack okaeriSerdesPack = null;

    public ConfigurationFactory(@NotNull File dataDirectory, Configurer configurer, OkaeriSerdesPack okaeriSerdesPack) {
        this.dataDirectory = dataDirectory;
        this.configurer = configurer;
        this.okaeriSerdesPack = okaeriSerdesPack;
    }

    public ConfigurationFactory(@NotNull File dataDirectory, Configurer configurer) {
        this.dataDirectory = dataDirectory;
        this.configurer = configurer;
    }

    public <T extends OkaeriConfig> T produce(@NotNull Class<T> type, @NotNull String fileName) {
        return this.produce(type, new File(this.dataDirectory, fileName));
    }

    public <T extends OkaeriConfig> T produce(@NotNull Class<T> type, @NotNull File file) {
        return ConfigManager.create(type, it -> {
            it.withConfigurer(this.configurer).withBindFile(file).saveDefaults();
            if (this.okaeriSerdesPack != null) it.withSerdesPack(this.okaeriSerdesPack);
            it.load(true);
        });
    }
}