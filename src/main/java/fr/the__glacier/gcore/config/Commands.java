package fr.the__glacier.gcore.config;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;

import java.util.List;
import java.util.Map;

public class Commands {
    public CommandConfig GCoreCMD;
    public String noPermission;

    public Commands(){
        noPermission = "You don't have permission to do that !";
        Map<String, SubCommandConfig> map = ImmutableMap.of(
                "database", new SubCommandConfig(
                        List.of("database", "databases", "db"),
                        "Accéder à la database.",
                        "/testcmd db",
                        "perm",
                        "You don't have permission to do that !",
                        0L,
                        true)
        );
        GCoreCMD = new CommandConfig("A test command", "/testCmd", "permission", noPermission, 0L, true, map);
    }

}
