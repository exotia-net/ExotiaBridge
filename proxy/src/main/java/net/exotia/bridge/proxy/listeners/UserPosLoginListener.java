package net.exotia.bridge.proxy.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.minecodes.minelogin.bungee.api.event.post.UserPostLoginEvent;

public class UserPosLoginListener implements Listener {
    @EventHandler
    public void onLogin(UserPostLoginEvent event) {
        ProxiedPlayer player = event.getProxiedPlayer();
        //player.disconnect(player.getUniqueId() + " " + event.getUser().getUuid());
        player.sendMessage(player.getUniqueId() + " " + event.getUser().getUuid());
    }
}
