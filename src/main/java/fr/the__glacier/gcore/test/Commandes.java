package fr.the__glacier.gcore.test;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.database.UserTable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public class Commandes implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 0) return false;
        String arg1 = args[0];
        if (arg1.equalsIgnoreCase("database")){
            if (args.length == 1) return false;
            String arg2 = args[1];
            if (arg2.equalsIgnoreCase("user")){
                if (args.length == 2) return false;
                String arg3 = args[2];
                UserTable userTable = GCore.getInstance().getUserTable();
                if (arg3.equalsIgnoreCase("info")){
                    if (args.length == 3) return false;
                    String arg4 = args[3];
                    UserTable.User u = userTable.getUser(arg4);
                    if (u != null){
                        sender.sendMessage("Pseudo : " + u.getName());
                        sender.sendMessage("UUID : " + u.getUuid());
                        Calendar calendar = Calendar.getInstance();
                        Date first = new Date(u.getFirstJoinTime());
                        calendar.setTime(first);
                        sender.sendMessage("First time join : " + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) +1)) + "-" + calendar.get(Calendar.YEAR));
                        Date last = new Date(u.getLastJoinTime());
                        calendar.setTime(last);
                        sender.sendMessage("Last time join : " + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) +1)) + "-" + calendar.get(Calendar.YEAR) + " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)));
                        Date leave = new Date(u.getLastLeaveTime());
                        calendar.setTime(leave);
                        sender.sendMessage("Last time leave : " + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) +1)) + "-" + calendar.get(Calendar.YEAR) + " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND)));
                        sender.sendMessage("Online : " + (u.isOnline() ? "§aonline" : "§coffline"));
                    } else {
                        sender.sendMessage("Aucun utilisateur du nom de " + arg4 + " n'est dans la base de donnée.");
                    }
                }
            }
        }
        return false;
    }
}
