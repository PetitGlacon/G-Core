package fr.the__glacier.gcore.commands.gcore;

import com.mojang.brigadier.context.CommandContext;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.commands.utils.BrigadierCommands;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.commands.gcore.subcmd.Databases;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.config.configObjects.SimpleItemConfig;
import fr.the__glacier.gcore.config.configObjects.gui.Background;
import fr.the__glacier.gcore.config.configObjects.gui.ItemGUIConfig;
import fr.the__glacier.gcore.config.configObjects.gui.PagedGUIConfig;
import fr.the__glacier.gcore.config.configObjects.gui.SimpleGUIConfig;
import fr.the__glacier.gcore.gui.PagedGUI;
import fr.the__glacier.gcore.util.PagedMessage;
import fr.the__glacier.gcore.util.PlayerUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GCoreCommand extends BrigadierCommands {

    public GCoreCommand(GCore plugin, SubCommandsManager cmdManager, CommandConfig command){
        super(plugin, cmdManager, command);
        if (command.subCommands != null){
            this.commandsManager.registerSubCommand(new Databases(command.subCommands.get("database"), this.cooldownManager, command.isOnCooldown));
        }
        registerCommand();
        this.command.executes(this::test);
    }

    public int test(CommandContext<CommandSourceStack> context){
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player){
            PagedGUIConfig pagedGUIConfig = new PagedGUIConfig(
                    "<red>Bonjour !",
                    InventoryType.CHEST,
                    27,
                    new Background(
                            new SimpleItemConfig(
                                    "minecraft",
                                    "emerald",
                                    "itemName",
                                    2,
                                    List.of("<red>Lore ligne 1", "<green>Lore ligne 2 !"),
                                    new SimpleItemConfig.ItemOptions(null, true, null, false)),
                            List.of(10,11,12,13,14,15,16),
                            Map.of(11, new SimpleItemConfig(
                                    "minecraft",
                                    "egg",
                                    "itemName",
                                    16,
                                    List.of("<red>Lore ligne 1", "<green>Lore ligne 2 !"),
                                    new SimpleItemConfig.ItemOptions(null, null, null, false))
                            )
                    ),
                    new ItemGUIConfig(
                            "minecraft",
                            "arrow",
                            "previous",
                            1,
                            List.of("<red>Page %page%", "<green>Max page %maxPage%"),
                            new SimpleItemConfig.ItemOptions(null, true, null, false),
                            18),
                    new ItemGUIConfig(
                            "minecraft",
                            "arrow",
                            "next",
                            2,
                            List.of("<red>Page %page%", "<green>Max page %maxPage%"),
                            new SimpleItemConfig.ItemOptions(null, true, null, false),
                            26)
            );
            PagedGUI pagedGUI = new PagedGUI(pagedGUIConfig);
            player.openInventory(pagedGUI.getInventory());
//            PlayerUtil.sendMiniMessage(player, "<hover:show_text:'test'>test</hover>");
//            PagedMessage message = GCore.getInstance().getPagedMessagesManager().getPagedMessages().getFirst();
//            if (message != null) message.sendMessage(player, 1);
        }
        return 0;
    }
}
