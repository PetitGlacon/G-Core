package fr.the__glacier.gcore.commands.utils.deprecated;

import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Deprecated
public class BukkitCommands implements CommandExecutor, TabCompleter {
    @Getter
    public CommandConfig command;
    @Getter
    public SubCommandsManager1 commandsManager;
    public Plugin plugin;
    public TimeUtil.CooldownManager cooldownManager;

    public BukkitCommands(Plugin plugin, SubCommandsManager1 commandsManager, CommandConfig command){
        this.command = command;
        this.commandsManager = commandsManager;
        this.plugin = plugin;
        if (this.command.cooldownInSeconds != 0){
            this.cooldownManager = new TimeUtil.CooldownManager(this.command.cooldownInSeconds);
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (!checkPermission(sender)){
            sender.sendMessage(new MiniMessages(getCommand().noPermission).getComponent());
        }else if (checkCooldown(sender)){
            sender.sendMessage(new MiniMessages(getCommand().isOnCooldown.replace("%time%", TimeUtil.getDurationFormated(cooldownManager.timeUntilEndCooldown(sender)))).getComponent());
        } else {
            boolean b = executeSubCommand(sender, command, alias, args);
            if (!b) {
                sender.sendMessage(new MiniMessages(getCommand().syntax).getComponent());
            } else if (cooldownManager != null) {
                cooldownManager.addCooldown(sender);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 0){
            tab.addAll(commandsManager.getCommandMap().keySet());
        } else if (args.length == 1){
            String var1 = args[0];
            tab.addAll(commandsManager.getCommandMap().keySet().stream().filter(key -> key.startsWith(var1)).toList());
        } else {
            String arg = args[0];
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(arg);
            if (subCommandInterface != null && sender.hasPermission(subCommandInterface.getSubCommandConfig().permission)){
                String start = args[args.length -1];
                List<String> list = subCommandInterface.onTabComplete(plugin, sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                if (list != null){
                    tab.addAll(list.stream().filter(str -> str != null && str.startsWith(start)).toList());
                }
            }
        }
        return tab;
    }
    public boolean checkPermission(CommandSender sender){return sender.hasPermission(getCommand().permission);}
    public boolean checkCooldown(CommandSender sender) {if (cooldownManager == null) return false; return cooldownManager.isOnCooldown(sender);}
    public boolean executeSubCommand(CommandSender sender, Command command, String alias, String[] args){
        boolean b = false;
        if (args.length > 0){
            String subCommand = args[0].toLowerCase();
            String[] listArgs = Arrays.copyOfRange(args, 1, args.length);
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(subCommand);
            if (subCommandInterface != null){
                if (!sender.hasPermission(subCommandInterface.getSubCommandConfig().permission)){
                    sender.sendMessage(new MiniMessages(subCommandInterface.getSubCommandConfig().noPermission).getComponent());
                } else {
                    b = subCommandInterface.onCommand(plugin, sender, command, alias, listArgs);
                }
            }
        }
        return b;
    }
}
