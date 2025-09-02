package fr.the__glacier.gcore.commands.utils.deprecated;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class SubCommandsManager1 {
    private final Map<String, SubCommandInterface> subCommands = new HashMap<>();
    public void registerSubCommand(@NotNull SubCommandInterface subCommand) {
        if (!subCommand.getSubCommandConfig().enabled) return;
        List<String> list = subCommand.getSubCommandConfig().aliases;
        for (String str : list){
            subCommands.put(str, subCommand);
        }
    }

    public SubCommandInterface getSubCommand(@NotNull String name) {
        return subCommands.getOrDefault(name, null);
    }

    public Map<String, SubCommandInterface> getCommandMap() {
        return subCommands;
    }
}
