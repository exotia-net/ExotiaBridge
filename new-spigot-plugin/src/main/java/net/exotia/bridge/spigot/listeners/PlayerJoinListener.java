package net.exotia.bridge.spigot.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;

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
            }
            return null;
        }).join();
    }
}
