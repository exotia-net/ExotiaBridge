package net.exotia.bridge.spigot.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;
    @Inject private Plugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.userService.getUser(player.getUniqueId()) != null) return;
        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(player.getUniqueId(), player.getName());

        this.userService.authorize(exotiaPlayer).thenAccept(user -> {
            this.userService.requestBalance(user.getUuid());
        }).handle((result, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                player.sendMessage(MessageService.getFormattedMessage(exception.getMessage()));
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.sendMessage("test");
                    player.kickPlayer(MessageService.getFormattedMessage(exception.getMessage()));
                }, 20L);
            }
            return null;
        });
    }
}
