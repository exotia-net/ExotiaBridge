package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.configuration.proxy.ProxyConfiguration;
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
    @Inject private ProxyConfiguration configuration;

    @EventHandler
    public void onPlayerLogIn(UserPreLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        SocketAddress address = player.getSocketAddress();
        String ip = address instanceof InetSocketAddress ? ((InetSocketAddress) address).getHostString() : "0.0.0.0";
        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(player.getUniqueId(), player.getName(), ip);

        this.userService.authorize(exotiaPlayer).thenAccept(user -> {
            if (user != null) return;
            this.userService.signUp(exotiaPlayer).thenAccept(isSuccess -> {
                player.disconnect(new TextComponent(MessageService.getFormattedMessage(this.configuration.getUserCreatedMessage())));
            }).handle((result, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                    String message = this.configuration.getApiErrorMessage(exception.getMessage());
                    player.disconnect(new TextComponent(MessageService.getFormattedMessage(message)));
                }
                return null;
            });
        }).handle((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                String message = this.configuration.getApiErrorMessage(exception.getCause().getMessage());
                player.disconnect(new TextComponent(MessageService.getFormattedMessage(message)));
            }
            return null;
        }).join();
    }
}
