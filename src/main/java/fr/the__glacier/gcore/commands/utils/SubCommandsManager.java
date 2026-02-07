package fr.the__glacier.gcore.commands.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommandsManager {
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    public void registerSubCommand(@NotNull SubCommand subCommand) {
        if (!subCommand.getCommandConfig().enabled) return;
        subCommands.put(subCommand.getCommandConfig().name, subCommand);
        List<String> alias = subCommand.getCommandConfig().alias;
        if (alias == null) return;
        for (String str : alias){
            subCommands.put(str, subCommand);
        }
    }

    public SubCommand getSubCommand(@NotNull String name) {
        return subCommands.get(name);
    }

    public Map<String, SubCommand> getCommandMap() {
        return subCommands;
    }
}
