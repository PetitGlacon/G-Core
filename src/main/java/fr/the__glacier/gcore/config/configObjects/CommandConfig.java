package fr.the__glacier.gcore.config.configObjects;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CommandConfig {
    public CommandConfig(){
        description = "";
        syntax = "";
        permission = "";
        noPermission = "";
        cooldownInSeconds = 0;
        enabled = false;
        subCommands = null;
    }
    public CommandConfig(String description, String syntax, String permission, String noPermission, Long cooldownInSeconds, boolean enabled, @Nullable Map<String,SubCommandConfig> subCommands){
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.enabled = enabled;
        this.subCommands = subCommands;
    }
    public CommandConfig(String description, String syntax, String permission, String noPermission, Long cooldownInSeconds, boolean enabled){
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.enabled = enabled;
        this.subCommands = null;
    }

    public String description;
    public String syntax;
    public String permission;
    public String noPermission;
    public long cooldownInSeconds;
    public boolean enabled;
    @Nullable
    public Map<String,SubCommandConfig> subCommands;
}
