package net.exotia.bridge.plugin.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.userService.getUser(player.getUniqueId()) != null) return;

        this.userService.isAuthorized(new ExotiaPlayer(player.getUniqueId(), player.getName(), "0.0.0.0"), ((aBoolean, user) -> {
            this.userService.getPlayerBalance(user.getExotiaPlayer()).thenAccept(user::setBalance);
        }));
    }
}
