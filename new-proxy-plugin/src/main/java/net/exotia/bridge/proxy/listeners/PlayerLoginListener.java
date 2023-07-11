package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.proxy.messaging.BungeeMessagingService;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class PlayerLoginListener implements Listener {
    @Inject private BungeeMessagingService bungeeMessagingService;
    @Inject private Plugin plugin;

    @EventHandler
    public void onLogin(PostLoginEvent event) {
//        ProxiedPlayer player = event.getPlayer();
//        this.plugin.getProxy().getServers().forEach((key, serverInfo) -> {
//            this.bungeeMessagingService.sendMessageData(serverInfo, MessagingChannels.SEND_TOKEN,
//                    new TokenPacket(this.tokenStorage.getToken(), player.getUniqueId().toString())
//            );
//        });
//        System.out.println("======================= [LoginEvent] =======================");
//        System.out.println(this.plugin.getProxy().getPlayers().stream().map(ProxiedPlayer::getDisplayName).collect(Collectors.joining()));
//        System.out.println("======================= [LoginEvent] =======================");
//        this.bungeeMessagingService.sendMessageData(player, MessagingChannels.SEND_TOKEN,
//                new TokenPacket(this.tokenStorage.getToken(), player.getUniqueId().toString())
//        );
    }
}
