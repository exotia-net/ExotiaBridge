package net.exotia.bridge.plugin.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.messaging_api.packets.spigot.UserNeedUpdatePacket;
import net.exotia.bridge.plugin.service.SpigotMessagingService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;
    @Inject private SpigotMessagingService spigotMessagingService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.userService.getUser(player.getUniqueId()) != null) return;

        this.userService.isAuthorized(new ExotiaPlayer(player.getUniqueId(), player.getName(), "0.0.0.0"), ((aBoolean, user) -> {
            this.userService.getPlayerBalance(user.getExotiaPlayer()).thenAccept(balance -> {
                user.setBalance(balance);
                System.out.println("===========");
                System.out.println(balance);
                System.out.println("===========");
                this.spigotMessagingService.sendMessageData(player, MessagingChannels.USER_NEED_UPDATE, new UserNeedUpdatePacket(user.getFirstIp() + " | " + user.getBalance()));
            });
        }));
    }
}
