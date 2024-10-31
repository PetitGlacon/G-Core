package fr.the__glacier.gcore.util;


import fr.the__glacier.gcore.color.ColorsUtil;
import fr.the__glacier.gcore.color.MiniMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerUtil {
    public static void sendColoredMessage(Player p, String str){
        p.sendMessage(ColorsUtil.color(str));
    }
    public static void sendMiniMessage(CommandSender e, String str){
        CompletableFuture.runAsync(() -> {
            MiniMessages msg = new MiniMessages(str);
            e.sendMessage(msg.getComponent());
        });
    }
    public static void broadcastMiniMessage(String str){
        CompletableFuture.runAsync(() -> {
            MiniMessages msg = new MiniMessages(str);
            Bukkit.broadcast(msg.getComponent());
        });
    }
    public static void broadcastMiniMessage(Component str){
        CompletableFuture.runAsync(() -> Bukkit.broadcast(str));
    }

    public static void playsound(Player p, Sound sound){
        p.playSound(p, sound, SoundCategory.AMBIENT, 1, 1);
    }
}
