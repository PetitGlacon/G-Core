package fr.the__glacier.gcore.commands.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class Command {
    @Getter
    protected CommandConfig commandConfig;
    @Getter
    protected SubCommandsManager commandsManager;
    public Plugin plugin;
    @Getter
    protected TimeUtil.CooldownManager cooldownManager;

    @Getter
    public LiteralArgumentBuilder<CommandSourceStack> command;

    public Command(Plugin plugin, SubCommandsManager commandsManager, CommandConfig command){
        this.commandConfig = command;
        this.commandsManager = commandsManager;
        this.plugin = plugin;
        if (this.commandConfig.cooldownInSeconds != 0){
            this.cooldownManager = new TimeUtil.CooldownManager(this.commandConfig.cooldownInSeconds);
        }
    }
    public Command(Plugin plugin, CommandConfig command){
        this.commandConfig = command;
        this.commandsManager = new SubCommandsManager();
        this.plugin = plugin;
        if (this.commandConfig.cooldownInSeconds != 0){
            this.cooldownManager = new TimeUtil.CooldownManager(this.commandConfig.cooldownInSeconds);
        }
    }

    public void registerCommand(){
        String name = Objects.requireNonNullElse(this.commandConfig.name, this.commandConfig.toString());
        command = io.papermc.paper.command.brigadier.Commands.literal(name).requires(sender -> checkPermission(sender.getSender()));
        if (commandsManager == null) return;
        Map<String, SubCommand> map = commandsManager.getCommandMap();
        if (map == null) return;
        for (Map.Entry<String, SubCommand> entry : map.entrySet()){
            LiteralArgumentBuilder<CommandSourceStack> child = entry.getValue().getCommand(entry.getKey());
            assert this.command != null;
            this.command.then(child);
        }
    }
    public void addCooldown(CommandSender sender){
        if (cooldownManager != null){
            cooldownManager.addCooldown(sender);
        }
    }
    public boolean isOnCooldown(CommandSender sender){
        boolean b = false;
        if (cooldownManager != null){
            b = cooldownManager.isOnCooldown(sender);
        }
        if (b) sendOnCooldown(sender);
        return b;
    }
    public long timeRemainingCooldown(CommandSender sender){
        long l = 0;
        if (cooldownManager != null){
            l = cooldownManager.timeUntilEndCooldown(sender);
        }
        l = Math.round((float) l /1000);
        return l;
    }
    public void sendOnCooldown(CommandSender sender){
        String message = this.commandConfig.isOnCooldown;
        message = message.replace("%isOnCooldown%", GCore.getInstance().getCommands().isOnCooldown);
        sender.sendMessage(new MiniMessages(message
                .replace("%time%", String.valueOf(timeRemainingCooldown(sender)))
                .replace("%time_formatted%", TimeUtil.getDurationFormated(timeRemainingCooldown(sender) * 1000))).getComponent());
    }

    public CommandConfig getSubCommandConfig(String string){
        if (commandConfig.subCommands == null) {
            plugin.getLogger().severe("No Subcommands in the configuration !");
            return null;
        }
        CommandConfig config = commandConfig.subCommands.get(string);
        if (config == null) plugin.getLogger().severe("Missing " + string +" subCommand in the configuration !");
        return config;
    }
    public boolean checkPermission(CommandSender sender){return sender.hasPermission(getCommandConfig().permission);}
}
