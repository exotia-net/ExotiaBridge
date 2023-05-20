package net.exotia.bridge.proxy.listeners;

import eu.okaeri.injector.annotation.Inject;
import net.exotia.bridge.messaging_api.channel.MessagingChannels;
import net.exotia.bridge.messaging_api.packets.UpdateWalletPacket;
import net.exotia.bridge.messaging_api.packets.UserNeedUpdatePacket;
import net.exotia.bridge.proxy.configuration.PluginConfiguration;
import net.exotia.bridge.proxy.service.BungeeMessagingService;
import net.exotia.bridge.shared.messages.MessageService;
import net.exotia.bridge.shared.services.UserService;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.minecodes.minelogin.bungee.api.event.pre.UserPreLoginEvent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UserPostLoginListener implements Listener {
    @Inject private UserService userService;
    @Inject private PluginConfiguration configuration;
    @Inject private BungeeMessagingService bungeeMessagingService;

    @EventHandler
    public void onLogin(UserPreLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        SocketAddress address = player.getSocketAddress();

        ExotiaPlayer exotiaPlayer = new ExotiaPlayer(
                player.getUniqueId(), player.getName(),
                address instanceof InetSocketAddress ? ((InetSocketAddress) address).getHostString() : "0.0.0.0"
        );

        if (this.userService.getUser(player.getUniqueId()) != null) return;
        this.userService.isAuthorized(exotiaPlayer, (result, user) -> {
            if (result) {
                this.userService.getWallet(exotiaPlayer).thenAccept(walletResponse -> {
                    user.setCoins(walletResponse.getCoins());
                    this.bungeeMessagingService.sendMessageData(player.getServer(), MessagingChannels.USER_WALLET_UPDATED, new UpdateWalletPacket(exotiaPlayer.getUniqueIdString(), user.getCoins()));
                });
                return;
            }
            this.userService.signUp(exotiaPlayer, (isSuccess, responseMessage) -> {
                String message = isSuccess ? this.configuration.getUserCreatedMessage() : this.configuration.getApiErrorMessage(0, responseMessage);
                player.disconnect(new TextComponent(MessageService.getFormattedMessage(message)));
            });
        });
    }
}
