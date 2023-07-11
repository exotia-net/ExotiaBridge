package net.exotia.bridge.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.configuration.proxy.ProxyConfiguration;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import pl.minecodes.minelogin.velocity.api.event.pre.UserPreLoginEvent;

import java.net.InetAddress;

public class UserPostLoginListener {
    @Inject private UserService userService;
    @Inject private ProxyConfiguration configuration;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    @Subscribe
    public void onLogin(UserPreLoginEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        InetAddress address = player.getRemoteAddress().getAddress();
        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(player.getUniqueId(), player.getUsername(), address.getHostAddress());

        this.userService.authorize(exotiaPlayer).thenAccept(user -> {
            if (user != null) return;
            this.userService.signUp(exotiaPlayer).thenAccept(isSuccess -> {
                player.disconnect(this.miniMessage.deserialize(this.configuration.getUserCreatedMessage()));
            }).handle((result, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                    String message = this.configuration.getApiErrorMessage(exception.getMessage());
                    player.disconnect(this.miniMessage.deserialize(message));
                }
                return null;
            });
        }).handle((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                String message = this.configuration.getApiErrorMessage(exception.getCause().getMessage());
                player.disconnect(this.miniMessage.deserialize(message));
            }
            return null;
        }).join();
    }
}
