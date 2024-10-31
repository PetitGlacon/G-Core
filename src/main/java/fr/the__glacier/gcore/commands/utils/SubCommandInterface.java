package fr.the__glacier.gcore.commands.utils;

import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SubCommandInterface {
    boolean onCommand(@NotNull Plugin plugin, @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args);
    @Nullable
    List<String> onTabComplete(@NotNull Plugin plugin, @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args);
    SubCommandConfig getSubCommandConfig();
}
