package net.exotia.bridge.plugin.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.plugin.user.User;
import net.exotia.bridge.plugin.user.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.net.InetSocketAddress;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;
    @Inject private Plugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

//        InetSocketAddress IPAdressPlayer = player.getAddress();
//        String sfullip = IPAdressPlayer.toString();
//        String[] fullip;
//        String[] ipandport;
//        fullip = sfullip.split("/");
//        String sIpandPort = fullip[1];
//        ipandport = sIpandPort.split(":");
//        String sIp = ipandport[0];
//
//        player.sendMessage("You logged in with the ip: " + sIp);
        player.sendMessage(player.getUniqueId().toString());
//        if (this.userService.getUser(player.getUniqueId()) != null) return;
//
//        player.sendMessage("Autoryzowanie...");
//        this.userService.isAuthorized(player, result -> {
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
//        });

//        User user = User.builder()
//                .uniqueId(player.getUniqueId())
//                .lastIp(player.ge)
//                .build();
    }
}
