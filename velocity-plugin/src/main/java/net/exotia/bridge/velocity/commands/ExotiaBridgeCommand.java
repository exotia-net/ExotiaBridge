package net.exotia.bridge.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Route(name = "bridge")
public class ExotiaBridgeCommand {
    @Inject private ProxyServer proxyServer;

    @Execute(route = "send")
    public void send(@Arg Player player, @Arg RegisteredServer server) {
        player.createConnectionRequest(server).connect();
    }

    @Execute(route = "announce")
    public void announce(@Name("message") @Joiner String message) {
        this.proxyServer.getAllPlayers().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }
}
