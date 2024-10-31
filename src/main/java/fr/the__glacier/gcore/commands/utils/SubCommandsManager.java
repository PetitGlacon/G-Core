package fr.the__glacier.gcore.commands.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class SubCommandsManager {
    public abstract void registerSubCommand(@NotNull SubCommandInterface subCommand);

    public abstract SubCommandInterface getSubCommand(@NotNull String name);

    public abstract Map<String, SubCommandInterface> getCommandMap();
}
