package fr.the__glacier.gcore.commands.utils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;


public abstract class SubCommand {
    public SubCommandConfig config;

    public TimeUtil.CooldownManager mainCooldownManager;
    private final TimeUtil.CooldownManager cooldownManager;
    private final String cooldownMessage;

    public SubCommand(SubCommandConfig config, TimeUtil.CooldownManager mainCooldownManager, String cooldownMessage){
        this.config = config;
        this.mainCooldownManager = mainCooldownManager;
        this.cooldownMessage = cooldownMessage;
        if (config.cooldownInSeconds > 0){
            this.cooldownManager = new TimeUtil.CooldownManager(config.cooldownInSeconds);
        } else {
            this.cooldownManager = null;
        }
    }
    public void addCooldown(Object o){
        if (cooldownManager != null){
            cooldownManager.addCooldown(o);
        }
        if (mainCooldownManager != null){
            mainCooldownManager.addCooldown(o);
        }
    }
    public boolean isOnCooldown(Object o){

        boolean b = false;
        if (cooldownManager != null){
            b = cooldownManager.isOnCooldown(o);
        }
        if (mainCooldownManager != null){
            b = b || mainCooldownManager.isOnCooldown(o);
        }
        return b;
    }
    public long timeRemainingCooldown(Object o){
        long l = 0;
        if (cooldownManager != null){
            l = cooldownManager.timeUntilEndCooldown(o);
        }
        if (mainCooldownManager != null){
            l = Math.max(l, mainCooldownManager.timeUntilEndCooldown(o));
        }
        l = Math.round((float) l /1000);
        return l;
    }
    public void sendOnCooldown(CommandSender sender){
        sender.sendMessage(new MiniMessages(this.cooldownMessage
                .replace("%time%", String.valueOf(timeRemainingCooldown(sender)))
                .replace("%time_formatted%", TimeUtil.getDurationFormated(timeRemainingCooldown(sender) * 1000))).getComponent());
    }

    public boolean checkCooldown(CommandSender sender){
        if (isOnCooldown(sender)){
            sendOnCooldown(sender);
            return true;
        }
        return false;
    }

    public abstract LiteralArgumentBuilder<CommandSourceStack> getCommand(String alias);

    public SubCommandConfig getSubCommandConfig(){
        return this.config;
    }
}
