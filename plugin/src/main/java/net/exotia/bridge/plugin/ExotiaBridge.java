package net.exotia.bridge.plugin;

import eu.okaeri.injector.OkaeriInjector;
import net.exotia.bridge.plugin.http.HttpService;
import net.exotia.bridge.plugin.listeners.PlayerJoinListener;
import net.exotia.bridge.plugin.user.UserService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExotiaBridge extends JavaPlugin {
    private final OkaeriInjector injector = OkaeriInjector.create();

    @Override
    public void onEnable() {
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.injector.registerInjectable(this.injector.createInstance(HttpService.class));
        this.injector.registerInjectable(this.injector.createInstance(UserService.class));

        this.getServer().getPluginManager().registerEvents(this.injector.createInstance(PlayerJoinListener.class), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
