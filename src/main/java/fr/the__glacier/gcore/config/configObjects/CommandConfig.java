package fr.the__glacier.gcore.config.configObjects;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CommandConfig {
    public CommandConfig(){
        description = "";
        syntax = "";
        permission = "";
        noPermission = "";
        cooldownInSeconds = 0;
        isOnCooldown = "";
        enabled = false;
        subCommands = null;
        this.alias = null;
    }
    public CommandConfig(String name){
        this.name = name;
        description = "";
        syntax = "";
        permission = "";
        noPermission = "";
        cooldownInSeconds = 0;
        isOnCooldown = "";
        enabled = false;
        subCommands = null;
        this.alias = null;
    }
    public CommandConfig(String description,
                         String syntax,
                         String permission,
                         String noPermission,
                         Long cooldownInSeconds,
                         String isOnCooldown,
                         boolean enabled,
                         @Nullable Map<String,SubCommandConfig> subCommands){
        this.name = "cmd";
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.isOnCooldown = isOnCooldown;
        this.enabled = enabled;
        this.subCommands = subCommands;
    }
    public CommandConfig(String description,
                         String syntax,
                         String permission,
                         String noPermission,
                         Long cooldownInSeconds,
                         String isOnCooldown,
                         boolean enabled){
        this.name = "cmd";
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.isOnCooldown = isOnCooldown;
        this.enabled = enabled;
        this.subCommands = null;
    }
    public CommandConfig(String name,
                         String description,
                         String syntax,
                         String permission,
                         String noPermission,
                         Long cooldownInSeconds,
                         String isOnCooldown,
                         boolean enabled,
                         @Nullable Map<String,SubCommandConfig> subCommands,
                         @Nullable List<String> alias){
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.isOnCooldown = isOnCooldown;
        this.enabled = enabled;
        this.subCommands = subCommands;
        this.alias = alias;
    }

    public String name;
    public String description;
    public String syntax;
    public String permission;
    public String noPermission;
    public long cooldownInSeconds;
    public String isOnCooldown;
    public boolean enabled;
    public List<String> alias;
    @Nullable
    public Map<String,SubCommandConfig> subCommands;
}
