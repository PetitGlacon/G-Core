package fr.the__glacier.gcore.config;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import it.unimi.dsi.fastutil.Hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {
    public CommandConfig GCoreCMD;
    public String noPermission;
    public String isOnCooldown;

    public Commands(){
        noPermission = "You don't have permission to do that !";
        isOnCooldown = "You cannot use this now ! Try again in %time%.";
        Map<String, CommandConfig> map = ImmutableMap.of(
                "database", new CommandConfig(
                        "database",
                        true,
                        List.of("database", "db"),
                        "Accéder à la database.",
                        "/gcore db",
                        "gcore.db",
                        noPermission,
                        0L,
                        isOnCooldown,
                        new HashMap<>(),
                        new HashMap<>())
        );
        GCoreCMD = new CommandConfig(
                "gcore",
                true,
                List.of(),
                "Main G-Core command",
                "/gcore",
                "gcore",
                noPermission,
                0L,
                isOnCooldown,
                new HashMap<>(),map);
    }

}
