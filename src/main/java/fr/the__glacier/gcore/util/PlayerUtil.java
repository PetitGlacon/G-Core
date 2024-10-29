package fr.the__glacier.gcore.util;


import fr.the__glacier.gcore.color.ColorsUtil;
import fr.the__glacier.gcore.color.MiniMessages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerUtil {
    public static void sendColoredMessage(Player p, String str){
        p.sendMessage(ColorsUtil.color(str));
    }
    public static void sendMiniMessage(Player p, String str){
        CompletableFuture.runAsync(() -> {
            MiniMessages msg = new MiniMessages(str);
            p.sendMessage(msg.getComponent());
        });
    }
    public static void broadcastMiniMessage(String str){
        CompletableFuture.runAsync(() -> {
            MiniMessages msg = new MiniMessages(str);
            Bukkit.broadcast(msg.getComponent());
        });
    }

    public static void playsound(Player p, Sound sound){
        p.playSound(p, sound, SoundCategory.AMBIENT, 1, 1);
    }
}
