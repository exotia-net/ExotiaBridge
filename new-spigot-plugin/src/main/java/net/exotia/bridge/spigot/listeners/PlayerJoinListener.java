package net.exotia.bridge.spigot.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.configuration.spigot.SpigotConfiguration;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.exotia.bridge.shared.services.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;
    @Inject private Plugin plugin;
    @Inject private SpigotConfiguration configuration;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (this.configuration.isEnableAddressChecker() && !this.configuration.getAddresses().contains(event.getHostname())) {
            String message = this.configuration.getAuthFailedMessage().replace("{hostname}", event.getHostname());
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageService.getFormattedMessage(message));
        }

        Player player = event.getPlayer();
        User user = this.userService.getUser(player.getUniqueId());
        if (user != null) {
            this.sendRequests(user);
            return;
        }
        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(player.getUniqueId(), player.getName());
        this.userService.authorize(exotiaPlayer).thenAccept(this::sendRequests).handle((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                player.sendMessage(MessageService.getFormattedMessage(exception.getMessage()));
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageService.getFormattedMessage(exception.getMessage()));
                }, 20L);
            }
            return null;
        });
    }

    private void sendRequests(User user) {
        this.userService.requestBalance(user.getUuid());
        this.userService.requestCalendar(user.getUuid());
    }
}
