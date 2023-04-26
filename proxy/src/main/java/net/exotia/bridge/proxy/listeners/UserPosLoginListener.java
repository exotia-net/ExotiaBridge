package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.minecodes.minelogin.bungee.api.event.pre.UserPreLoginEvent;

public class UserPosLoginListener implements Listener {
    @Inject private UserService userService;
    @Inject private PluginConfiguration configuration;

    @EventHandler
    public void onLogin(UserPreLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        if (this.userService.getUser(player.getUniqueId()) != null) return;
        this.userService.isAuthorized(player.getUniqueId(), player.getName(), (result, msg) -> {
            if (result) return;
            this.userService.signUp(player.getUniqueId(), player.getName(), (isSuccess, responseMessage) -> {
                String message = isSuccess ? this.configuration.getFormattedUserCreated() : this.configuration.getFormattedError(0, responseMessage);
                player.disconnect(new TextComponent(MessageService.getFormattedMessage(message)));
            });
        });
    }
}
