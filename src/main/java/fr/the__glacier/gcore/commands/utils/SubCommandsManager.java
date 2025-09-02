package fr.the__glacier.gcore.commands.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommandsManager {
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    public void registerSubCommand(@NotNull SubCommand subCommand) {
        if (!subCommand.getSubCommandConfig().enabled) return;
        List<String> list = subCommand.getSubCommandConfig().aliases;
        for (String str : list){
            subCommands.put(str, subCommand);
        }
    }

    public SubCommand getSubCommand(@NotNull String name) {
        return subCommands.getOrDefault(name, null);
    }

    public Map<String, SubCommand> getCommandMap() {
        return subCommands;
    }
}
