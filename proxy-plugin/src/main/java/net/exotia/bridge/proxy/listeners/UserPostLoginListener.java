package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.minecodes.minelogin.bungee.api.event.pre.UserPreLoginEvent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UserPostLoginListener implements Listener {
    @Inject private UserService userService;
    @Inject private PluginConfiguration configuration;

    @EventHandler
    public void onLogin(UserPreLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        SocketAddress address = player.getSocketAddress();

        ExotiaPlayer exotiaPlayer = ExotiaPlayer.builder()
                .uniqueId(player.getUniqueId())
                .username(player.getName())
                .ip(address instanceof InetSocketAddress ? ((InetSocketAddress) address).getHostString() : "0.0.0.0")
                .build();

        if (this.userService.getUser(player.getUniqueId()) != null) return;
        this.userService.isAuthorized(exotiaPlayer, (result, msg) -> {
            if (result) return;
            this.userService.signUp(exotiaPlayer, (isSuccess, responseMessage) -> {
                String message = isSuccess ? this.configuration.getFormattedUserCreated() : this.configuration.getFormattedError(0, responseMessage);
                player.disconnect(new TextComponent(MessageService.getFormattedMessage(message)));
            });
        });
    }
}