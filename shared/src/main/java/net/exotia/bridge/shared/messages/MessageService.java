package net.exotia.bridge.shared.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageService {
    public static String getFormattedMessage(String message) {
        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(message);
        return LegacyComponentSerializer.legacySection().serialize(parsed);
    }
}
