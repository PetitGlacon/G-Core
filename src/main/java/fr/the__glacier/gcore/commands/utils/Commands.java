package fr.the__glacier.gcore.commands.utils;

import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    @Getter
    public CommandConfig command;
    @Getter
    public SubCommandsManager commandsManager;
    public Plugin plugin;

    public Commands(CommandConfig command, SubCommandsManager commandsManager, Plugin plugin){
        this.command = command;
        this.commandsManager = commandsManager;
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0){
            String subCommand = args[0].toLowerCase();
            String[] listArgs = Arrays.copyOfRange(args, 1, args.length);
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(subCommand);
            if (subCommandInterface != null){
                return subCommandInterface.onCommand(plugin, sender, command, label, listArgs);
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1){
            String var1 = args[0];
            tab.addAll(commandsManager.getCommandMap().keySet().stream().filter(key -> key.startsWith(var1)).toList());
        } else {
            String arg = args[0];
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(arg);
            if (subCommandInterface != null){
                return subCommandInterface.onTabComplete(plugin, sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return tab;
    }
}
