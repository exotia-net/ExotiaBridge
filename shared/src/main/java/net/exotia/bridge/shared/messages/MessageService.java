package net.exotia.bridge.shared.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageService {
    public static String getFormattedMessage(String message) {
        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(message);
        return LegacyComponentSerializer.legacySection().serialize(parsed);
    }

    public static String formatLegacy(@NotNull String message) {
        return LegacyComponentSerializer.legacySection().serialize(Component.text(message));
    }
}
