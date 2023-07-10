package net.exotia.bridge.spigot.commands;

import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

@Route(name = "bridge")
@Permission("exotia.bridge.admin")
public class BridgeCommand {
    @Inject private Plugin plugin;

    @Execute(route = "notify")
    public void notify(CommandSender sender, @Joiner @Name("text") String text) {
        List<String> list = Arrays.stream(text.split("\\|")).toList();

    }
}
