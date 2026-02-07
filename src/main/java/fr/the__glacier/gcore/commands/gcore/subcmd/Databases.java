package fr.the__glacier.gcore.commands.gcore.subcmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.commands.utils.Command;
import fr.the__glacier.gcore.commands.utils.SubCommand;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import fr.the__glacier.gcore.database.UserTable;
import fr.the__glacier.gcore.util.TimeUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Databases extends SubCommand {
    public Databases(Command parrentCommand, Plugin plugin, CommandConfig subCommandConfig) {
        super(parrentCommand, plugin, subCommandConfig);
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand(String alias) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(alias);
        command.requires(sender -> (sender.getSender().hasPermission(this.commandConfig.permission)));

        command.then(Commands.literal("user")
                .then(Commands.literal("info")
                        .then(Commands.argument("user", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    Bukkit.getOnlinePlayers().forEach(player -> builder.suggest(player.getName()));
                                    return builder.buildFuture();
                                })
                                .executes(this::userinfo))));

        return command;
    }

    public int userinfo(CommandContext<CommandSourceStack> context){
        CommandSender sender = context.getSource().getSender();
        if (isOnCooldown(sender)) return 0;
        UserTable userTable = GCore.getInstance().getUserTable();
        String userName = context.getArgument("user", String.class);
        UserTable.User u = userTable.getUser(userName);
        if (u != null){
            sender.sendMessage("Pseudo : " + u.getName());
            sender.sendMessage("UUID : " + u.getUuid());
            sender.sendMessage("First time join : " + TimeUtil.getDateFormatedWithoutHMS(u.getFirstJoinTime()));
            sender.sendMessage("Last time join : " + TimeUtil.getDateFormated(u.getLastJoinTime()));
            sender.sendMessage("Last time leave : " + TimeUtil.getDateFormated(u.getLastLeaveTime()));
            sender.sendMessage("Online : " + (u.isOnline() ? "§aonline" : "§coffline"));
            addCooldown(sender);
            return 1;
        } else {
            sender.sendMessage("Aucun utilisateur du nom de " + userName + " n'est dans la base de donnée.");
            addCooldown(sender);
            return 1;
        }
    }
}
