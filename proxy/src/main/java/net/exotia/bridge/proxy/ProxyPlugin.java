package net.exotia.bridge.proxy;

import net.exotia.bridge.proxy.listeners.UserPosLoginListener;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxyPlugin extends Plugin {

    @Override
    public void onEnable() {
        this.getProxy().getPluginManager().registerListener(this, new UserPosLoginListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
