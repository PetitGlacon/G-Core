package fr.the__glacier.gcore.commands.gcore;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.commands.gcore.subcmd.Databases;
import fr.the__glacier.gcore.commands.utils.Commands;
import fr.the__glacier.gcore.commands.utils.SubCommandInterface;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GCoreCommand extends Commands {
    TimeUtil.CooldownManager cooldownManager;

    public GCoreCommand(GCore plugin, SubCommandsManager cmdManager, CommandConfig command){
        super(command, cmdManager, plugin);
        registerSubCommands();
        if (this.command.cooldownInSeconds != 0){
            cooldownManager = new TimeUtil.CooldownManager();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(getCommand().permission)) {
            sender.sendMessage(new MiniMessages(getCommand().noPermission).getComponent());
            return true;
        }
        if (cooldownManager != null){
            if (cooldownManager.isOnCooldown(sender)){
                sender.sendMessage("Tu es sous §4cooldown§r !");
                sender.sendMessage("Il te reste " + TimeUtil.getDurationFormated(cooldownManager.timeUntilEndCooldown(sender)) + ".");
                return true;
            }
        }
        if (args.length > 0){
            String subCommand = args[0].trim().toLowerCase();
            String[] listArgs = Arrays.copyOfRange(args, 1, args.length);
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(subCommand);
            if (subCommandInterface != null){
                if (!sender.hasPermission(subCommandInterface.getSubCommandConfig().permission)){
                    sender.sendMessage(new MiniMessages(subCommandInterface.getSubCommandConfig().noPermission).getComponent());
                    return true;
                }
                boolean result = subCommandInterface.onCommand(plugin, sender, command, label, listArgs);
                if (result){
                    if (cooldownManager != null) cooldownManager.addCooldown(sender, Duration.ofSeconds(this.command.cooldownInSeconds).toMillis());
                }
                return true;
            }
        }
        sender.sendMessage(new MiniMessages(this.command.syntax).getComponent());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1){
            tab.addAll(commandsManager.getCommandMap().keySet());
        } else {
            String arg = args[0];
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(arg);
            if (subCommandInterface != null){
                return subCommandInterface.onTabComplete(plugin, sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return tab;
    }

    public void registerSubCommands(){
        assert command.subCommands != null;
        commandsManager.registerSubCommand(new Databases(command.subCommands.get("database")));
    }
}
