package fr.the__glacier.gcore.commands.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public abstract class SubCommand extends Command{
    public Command parentCommand;

    public SubCommand(Command parentCommand, Plugin plugin, CommandConfig config){
        super(plugin, config);
        this.parentCommand = parentCommand;
    }
    @Override
    public void addCooldown(CommandSender sender){
        parentCommand.addCooldown(sender);
        if (cooldownManager != null){
            cooldownManager.addCooldown(sender);
        }
    }
    @Override
    public boolean isOnCooldown(CommandSender sender){
        boolean b = parentCommand.isOnCooldown(sender);
        if (b) return b;
        if (cooldownManager != null){
            b = cooldownManager.isOnCooldown(sender);
        }
        if (b) this.sendOnCooldown(sender);
        return b;
    }
    @Override
    public long timeRemainingCooldown(CommandSender sender){
        long l = parentCommand.timeRemainingCooldown(sender);
        if (cooldownManager != null){
            long ll = cooldownManager.timeUntilEndCooldown(sender);
            ll = Math.round((float) ll /1000);
            l = Math.max(l, ll);
        }
        return l;
    }

    public abstract LiteralArgumentBuilder<CommandSourceStack> getCommand(String alias);
}
