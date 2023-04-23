package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.shared.services.UserService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.minecodes.minelogin.bungee.api.event.post.UserPostLoginEvent;

public class UserPosLoginListener implements Listener {
    @Inject private UserService userService;

    @EventHandler
    public void onLogin(UserPostLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        //player.disconnect(player.getUniqueId() + " " + event.getUser().getUuid());
        player.sendMessage(player.getUniqueId() + " " + event.getUser().getUuid());
        this.userService.isAuthorized(player.getUniqueId(), player.getName(), (result, msg) -> {
            if (result) player.sendMessage("Autoryzowano pomyslnie!");
            else {
                player.sendMessage("Nie posiadasz konta!");
                player.sendMessage("Trwa tworzenie konta...");
                this.userService.signUp(player.getUniqueId(), player.getName(), (isSuccess, responseMessage) -> {
                    String message = isSuccess ? "Twoje konto zostalo pomyslnie utworzone \n Wbij ponownie na serwer" : "Wystapil niespodziwany blad po stronie serwera API! " + responseMessage;
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> player.kickPlayer(message));
                    player.disconnect(message);
                });
            }
        });
    }
}
