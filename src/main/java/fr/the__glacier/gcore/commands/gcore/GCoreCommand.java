package fr.the__glacier.gcore.commands.gcore;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.commands.gcore.subcmd.Databases;
import fr.the__glacier.gcore.commands.utils.Commands;
import fr.the__glacier.gcore.commands.utils.SubCommandInterface;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.PagedMessage;
import fr.the__glacier.gcore.util.PlayerUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GCoreCommand extends Commands {

    public GCoreCommand(GCore plugin, SubCommandsManager cmdManager, CommandConfig command){
        super(plugin, cmdManager, command);
        registerSubCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        boolean b = super.onCommand(sender, command, label, args);
        if (args.length == 0 && sender instanceof Player player){
            PlayerUtil.sendMiniMessage(player, "<hover:show_text:'test'>test</hover>");
            for (Map.Entry<UUID, PagedMessage> entry : GCore.getInstance().pagedMessagesMap.entrySet()){
                entry.getValue().sendMessage(player, 1);
                break;
            }
        }
        return b;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1){
            tab.addAll(commandsManager.getCommandMap().keySet());
        } else {
            String arg = args[0];
            SubCommandInterface subCommandInterface = commandsManager.getSubCommand(arg);
            if (subCommandInterface != null){
                return subCommandInterface.onTabComplete(plugin, sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return tab;
    }

    public void registerSubCommands(){
        assert command.subCommands != null;
        commandsManager.registerSubCommand(new Databases(command.subCommands.get("database")));
    }
}
