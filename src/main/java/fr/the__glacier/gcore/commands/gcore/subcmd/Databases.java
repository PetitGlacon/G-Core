package fr.the__glacier.gcore.commands.gcore.subcmd;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.commands.utils.SubCommandInterface;
import fr.the__glacier.gcore.config.configObjects.SubCommandConfig;
import fr.the__glacier.gcore.database.UserTable;
import fr.the__glacier.gcore.util.PlayerUtil;
import fr.the__glacier.gcore.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Databases implements SubCommandInterface {
    SubCommandConfig subCommandConfig;
    public Databases(SubCommandConfig subCommandConfig){
        this.subCommandConfig = subCommandConfig;
    }

    @Override
    public SubCommandConfig getSubCommandConfig() {
        return subCommandConfig;
    }
    @Override
    public boolean onCommand(@NotNull Plugin plugin, @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (!(plugin instanceof GCore gcore)) return false;
        if (!sender.hasPermission(subCommandConfig.permission)){
            String noPermission = subCommandConfig.noPermission;
            if (noPermission.equalsIgnoreCase("%nopermission%")){
                sender.sendMessage(new MiniMessages(gcore.getCommands().noPermission).getComponent());
            } else if (noPermission.equalsIgnoreCase("")) {
                return false;
            } else {
                sender.sendMessage(new MiniMessages(subCommandConfig.noPermission).getComponent());
            }
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("user")){
            if (args.length == 1) {
                sendSyntax(sender);
                return false;
            }
            String arg1 = args[1];
            UserTable userTable = gcore.getUserTable();
            if (arg1.equalsIgnoreCase("info")){
                if (args.length == 2) {
                    sendSyntax(sender);
                    return false;
                }
                String arg2 = args[2];
                UserTable.User u = userTable.getUser(arg2);
                if (u != null){
                    sender.sendMessage("Pseudo : " + u.getName());
                    sender.sendMessage("UUID : " + u.getUuid());
                    sender.sendMessage("First time join : " + TimeUtil.getDateFormatedWithoutHMS(u.getFirstJoinTime()));
                    sender.sendMessage("Last time join : " + TimeUtil.getDateFormated(u.getLastJoinTime()));
                    sender.sendMessage("Last time leave : " + TimeUtil.getDateFormated(u.getLastLeaveTime()));
                    sender.sendMessage("Online : " + (u.isOnline() ? "§aonline" : "§coffline"));
                    return true;
                } else {
                    sender.sendMessage("Aucun utilisateur du nom de " + arg2 + " n'est dans la base de donnée.");
                    return true;
                }
            }
        }
        sender.sendMessage(new MiniMessages(subCommandConfig.syntax).getComponent());
        return false;
    }
    public void sendSyntax(CommandSender sender){
        PlayerUtil.sendMiniMessage(sender, subCommandConfig.syntax);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Plugin plugin, @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1){
            return List.of("user");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("user")){
            return List.of("info");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("user") && args[1].equalsIgnoreCase("user")){
            List<String> list = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()){
                list.add(p.getName());
            }
            return list;
        }
        return null;
    }
}
