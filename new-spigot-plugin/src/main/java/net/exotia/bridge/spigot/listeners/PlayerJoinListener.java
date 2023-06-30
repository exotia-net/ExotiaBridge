package net.exotia.bridge.spigot.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.messaging_api.packets.TokenPacket;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.exotia.bridge.spigot.configuration.PluginConfiguration;
import net.exotia.bridge.spigot.messaging.SpigotMessagingService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {
    @Inject private UserService userService;
    @Inject private Plugin plugin;
    @Inject private SpigotMessagingService spigotMessagingService;
    @Inject private PluginConfiguration configuration;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!this.configuration.getAddresses().contains(event.getHostname())) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageService.getFormattedMessage("<red>Nie mozna autoryzowac polaczenia z adresu " + event.getHostname()));
        }

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
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageService.getFormattedMessage(exception.getMessage()));
                }, 20L);
            }
            return null;
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        if (this.userService.getUser(player.getUniqueId()) != null) return;
//        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(player.getUniqueId(), player.getName());
//
//        this.userService.authorize(exotiaPlayer).thenAccept(user -> {
//            this.userService.requestBalance(user.getUuid());
//        }).handle((result, exception) -> {
//            if (exception != null) {
//                exception.printStackTrace();
//                player.sendMessage(MessageService.getFormattedMessage(exception.getMessage()));
//                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
//                    player.sendMessage("test");
//                    player.kickPlayer(MessageService.getFormattedMessage(exception.getMessage()));
//                }, 20L);
//            }
//            return null;
//        });

    }
}
