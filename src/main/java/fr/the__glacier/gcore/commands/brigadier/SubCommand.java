package fr.the__glacier.gcore.commands.brigadier;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import fr.the__glacier.gcore.util.TimeUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;


public abstract class SubCommand {
    public SubCommandConfig config;

    public TimeUtil.CooldownManager mainCooldownManager;
    private final TimeUtil.CooldownManager cooldownManager;

    public SubCommand(SubCommandConfig config, TimeUtil.CooldownManager mainCooldownManager){
        this.config = config;
        this.mainCooldownManager = mainCooldownManager;
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
        if (cooldownManager != null){
            return cooldownManager.isOnCooldown(o);
        } else {
            return false;
        }
    }

    public abstract LiteralArgumentBuilder<CommandSourceStack> getCommand(String alias);

    public SubCommandConfig getSubCommandConfig(){
        return this.config;
    }
}
