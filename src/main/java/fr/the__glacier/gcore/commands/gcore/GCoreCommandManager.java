package fr.the__glacier.gcore.commands.gcore;

import fr.the__glacier.gcore.commands.utils.SubCommandInterface;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCoreCommandManager extends SubCommandsManager {
    private final Map<String, SubCommandInterface> subCommands = new HashMap<>();
    @Override
    public void registerSubCommand(@NotNull SubCommandInterface subCommand) {
        if (!subCommand.getSubCommandConfig().enabled) return;
        List<String> list = subCommand.getSubCommandConfig().aliases;
        for (String str : list){
            subCommands.put(str, subCommand);
        }
    }

    @Override
    public SubCommandInterface getSubCommand(@NotNull String name) {
        return subCommands.getOrDefault(name, null);
    }

    @Override
    public Map<String, SubCommandInterface> getCommandMap() {
        return subCommands;
    }
}
