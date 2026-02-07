package fr.the__glacier.gcore.config.configObjects;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandConfig {
    public CommandConfig(){
        this.name = "";
        enabled = false;
        this.alias = null;
        description = "";
        syntax = "";
        permission = "";
        noPermission = "";
        cooldownInSeconds = 0;
        isOnCooldown = "";
        messages = new HashMap<>();
        subCommands = new HashMap<>();
    }
    public CommandConfig(String name){
        this.name = name;
        enabled = false;
        this.alias = null;
        description = "";
        syntax = "";
        permission = "";
        noPermission = "";
        cooldownInSeconds = 0;
        isOnCooldown = "";
        messages = new HashMap<>();
        subCommands = new HashMap<>();
    }
    public CommandConfig(String name,
                         boolean enabled,
                         @Nullable List<String> alias,
                         String description,
                         String syntax,
                         String permission,
                         String noPermission,
                         Long cooldownInSeconds,
                         String isOnCooldown,
                         Map<String, String> messages,
                         @Nullable Map<String,CommandConfig> subCommands) {
        this.name = name;
        this.enabled = enabled;
        this.alias = alias;
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.isOnCooldown = isOnCooldown;
        this.messages = messages;
        this.subCommands = subCommands;
    }

    public String name;
    public boolean enabled;
    public List<String> alias;
    public String description;
    public String syntax;
    public String permission;
    public String noPermission;
    public long cooldownInSeconds;
    public String isOnCooldown;
    public Map<String, String> messages;
    @Nullable
    public Map<String,CommandConfig> subCommands;
}
