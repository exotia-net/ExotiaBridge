package net.exotia.bridge.plugin.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.services.UserService;
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

        player.sendMessage("Autoryzowanie...");
        this.userService.isAuthorized(player.getUniqueId(), player.getName(), (result, s) -> {
            player.sendMessage(String.valueOf(result));
//            if (result) player.sendMessage("Autoryzowano pomyslnie!");
//            else {
//                player.sendMessage("Nie posiadasz konta!");
//                player.sendMessage("Trwa tworzenie konta...");
//                this.userService.signUp(player, (isSuccess) -> {
//                    System.out.println("Status " + isSuccess);
//                    String message = isSuccess ? "Twoje konto zostalo pomyslnie utworzone \n Wbij ponownie na serwer" : "Wystapil niespodziwany blad po stronie serwera API";
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> player.kickPlayer(message));
//                });
//            }
        });

//        User user = User.builder()
//                .uniqueId(player.getUniqueId())
//                .lastIp(player.ge)
//                .build();
    }
}
