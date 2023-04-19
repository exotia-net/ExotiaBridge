package net.exotia.bridge.plugin.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.plugin.user.User;
import net.exotia.bridge.plugin.user.UserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.userService.isAuthorized(result -> player.sendMessage(String.valueOf(result)));

        player.sendMessage("test");
//        User user = User.builder()
//                .uniqueId(player.getUniqueId())
//                .lastIp(player.ge)
//                .build();
    }
}
