package fr.the__glacier.gcore.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.commands.utils.Command;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.util.PagedMessage;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GCoreUtils extends Command {

    public GCoreUtils(Plugin plugin, SubCommandsManager commandsManager, CommandConfig command) {
        super(plugin, commandsManager, command);
        registerCommand();
        this.command.then(io.papermc.paper.command.brigadier.Commands.argument("plugin", StringArgumentType.word())
                .then(io.papermc.paper.command.brigadier.Commands.argument("uuid", ArgumentTypes.uuid())
                        .then(io.papermc.paper.command.brigadier.Commands.argument("page", IntegerArgumentType.integer(0))
                                .executes(this::send)
                                .then(io.papermc.paper.command.brigadier.Commands.argument("player", ArgumentTypes.players()).suggests((context, builder) -> builder.buildFuture())
                                        .executes(this::sendPlayer))
                        )
                )
        ).requires(t -> true);
    }
    public GCoreUtils(Plugin plugin, CommandConfig command) {
        super(plugin, command);
        registerCommand();
        this.command.then(io.papermc.paper.command.brigadier.Commands.argument("plugin", StringArgumentType.word())
                .then(io.papermc.paper.command.brigadier.Commands.argument("uuid", ArgumentTypes.uuid())
                        .then(io.papermc.paper.command.brigadier.Commands.argument("page", IntegerArgumentType.integer(0))
                                .executes(this::send)
                                .then(io.papermc.paper.command.brigadier.Commands.argument("player", ArgumentTypes.players()).suggests((context, builder) -> builder.buildFuture())
                                        .executes(this::sendPlayer))
                        )
                )
        ).requires(t -> true);
    }


    public int send(CommandContext<CommandSourceStack> context){
        String plugin = context.getArgument("plugin", String.class);
        UUID uuid = context.getArgument("uuid", UUID.class);
        int page = context.getArgument("page", Integer.class);
        execute(plugin, uuid, page, context.getSource().getSender());

        return 1;
    }
    public int sendPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String plugin = context.getArgument("plugin", String.class);
        UUID uuid = context.getArgument("uuid", UUID.class);
        int page = context.getArgument("page", Integer.class);
        PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        Player player = resolver.resolve(context.getSource()).getFirst();
        execute(plugin, uuid, page, player);

        return 1;
    }

    public void execute(String plugin, UUID uuid, int page, CommandSender sender){
        Plugin pl = Bukkit.getPluginManager().getPlugin(plugin);
        PagedMessage pagedMessage = GCore.getInstance().getPagedMessagesManager().getPagedMessage(pl, uuid);
        if (pagedMessage == null) return;
        pagedMessage.sendMessage(sender, page);
    }
}
