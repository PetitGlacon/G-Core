package fr.the__glacier.gcore.commands.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class BrigadierCommands {
    @Getter
    public CommandConfig commandConfig;
    @Getter
    public SubCommandsManager commandsManager;
    public Plugin plugin;
    public TimeUtil.CooldownManager cooldownManager;

    @Getter
    public LiteralArgumentBuilder<CommandSourceStack> command;

    public BrigadierCommands(Plugin plugin, SubCommandsManager commandsManager, CommandConfig command){
        this.commandConfig = command;
        this.commandsManager = commandsManager;
        this.plugin = plugin;
        if (this.commandConfig.cooldownInSeconds != 0){
            this.cooldownManager = new TimeUtil.CooldownManager(this.commandConfig.cooldownInSeconds);
        }
    }

    public void registerCommand(){
        String name = Objects.requireNonNullElse(this.commandConfig.name, this.commandConfig.toString());
        command = Commands.literal(name).requires(sender -> checkPermission(sender.getSender()));
        if (commandsManager == null) return;
        Map<String, SubCommand> map = commandsManager.getCommandMap();
        if (map == null) return;
        for (Map.Entry<String, SubCommand> entry : map.entrySet()){
            LiteralArgumentBuilder<CommandSourceStack> child = entry.getValue().getCommand(entry.getKey());
            assert this.command != null;
            this.command.then(child);
        }
    }

    public boolean checkPermission(CommandSender sender){return sender.hasPermission(getCommandConfig().permission);}
}
