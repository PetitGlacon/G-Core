package fr.the__glacier.gcore.config.configObjects;

import java.util.Collections;
import java.util.List;

public class SubCommandConfig {
    public SubCommandConfig(){
        aliases = Collections.emptyList();
        description = "";
        syntax = "";
        permission = "";
        this.noPermission = "";
        cooldownInSeconds = 0;
        enabled = false;
    }
    public SubCommandConfig(List<String> aliases, String description, String syntax, String permission, String noPermission, Long cooldownInSeconds, boolean enabled){
        this.aliases = aliases;
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.noPermission = noPermission;
        this.cooldownInSeconds = cooldownInSeconds;
        this.enabled = enabled;
    }
    public List<String> aliases;
    public String description;
    public String syntax;
    public String permission;
    public String noPermission;
    public long cooldownInSeconds;
    public boolean enabled;
}
