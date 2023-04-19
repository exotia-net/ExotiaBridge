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

        if (this.userService.getUser(player.getUniqueId()) != null) return;

        player.sendMessage("Autoryzowanie...");
        this.userService.isAuthorized(result -> {
            if (result) player.sendMessage("Autoryzowano pomyslnie!");
            else {
                player.sendMessage("Nie posiadasz konta!");
                player.sendMessage("Trwa tworzenie konta...");
                this.userService.signUp(player, (isSuccess) -> {
                    if (isSuccess) player.sendMessage("Konto zostalo pomyslnie utworzone!");
                });
            }
        });

//        User user = User.builder()
//                .uniqueId(player.getUniqueId())
//                .lastIp(player.ge)
//                .build();
    }
}
