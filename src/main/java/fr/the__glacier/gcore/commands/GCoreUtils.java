package fr.the__glacier.gcore.commands;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.util.PagedMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GCoreUtils implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        GCore plugin = GCore.getInstance();
        if (args.length != 4 && args.length != 3){
            plugin.getLogger().severe("Failed to parse command : " + alias + " " + String.join(" ", args));
            return false;
        }
        Map<UUID, PagedMessage> pagedMessagesMap = plugin.getPagedMessagesMap();
        String pluginName = args[0];
        UUID uuid = UUID.fromString(args[1]);
        int page = Integer.parseInt(args[2]);
        if (args.length == 4){
            sender = Bukkit.getPlayer(args[3]);
        }
        PagedMessage pageMessages = pagedMessagesMap.get(uuid);
        pageMessages.sendMessage((Player) sender, page);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
