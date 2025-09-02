package fr.the__glacier.gcore.commands.gcore;

import com.mojang.brigadier.context.CommandContext;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.commands.utils.BrigadierCommands;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.commands.gcore.subcmd.Databases;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.PagedMessage;
import fr.the__glacier.gcore.util.PlayerUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GCoreCommand extends BrigadierCommands {

    public GCoreCommand(GCore plugin, SubCommandsManager cmdManager, CommandConfig command){
        super(plugin, cmdManager, command);
        this.commandsManager.registerSubCommand(new Databases(command.subCommands.get("database"), this.cooldownManager, command.isOnCooldown));
        registerCommand();
        this.command.executes(this::test);
    }

    public int test(CommandContext<CommandSourceStack> context){
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player){
            PlayerUtil.sendMiniMessage(player, "<hover:show_text:'test'>test</hover>");
            PagedMessage message = GCore.getInstance().getPagedMessagesManager().getPagedMessages().getFirst();
            if (message != null) message.sendMessage(player, 1);
        }
        return 0;
    }
}
