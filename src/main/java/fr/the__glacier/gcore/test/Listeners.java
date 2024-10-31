package fr.the__glacier.gcore.test;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.util.PlayerUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Objects;
import java.util.stream.Stream;


public class Listeners implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event){
        event.setCancelled(true);
        PlayerUtil.broadcastMiniMessage(event.message());
    }

    @EventHandler
    public void onPotionBrew(BrewEvent event){
        Bukkit.getScheduler().runTask(GCore.getInstance(), () -> {
            for (int i = 0; i < 3; i++) {
                ItemStack itemStack = event.getContents().getItem(i);
                if (itemStack != null && itemStack.getItemMeta() instanceof PotionMeta potionMeta) {
                    GCore.getInstance().getLogger().info(Objects.requireNonNull(potionMeta.getBasePotionType()).name() + " / " + potionMeta.getBasePotionType().isUpgradeable());
                    GCore.getInstance().getLogger().info(potionMeta.toString());
                }
            }
        });
    }
    @EventHandler
    public void onPlayerCommand(PlayerCommandSendEvent event){
        Stream<String> stream = event.getCommands().stream().filter(string -> string.contains("minecraft:") || string.contains("essentials:"));
        event.getCommands().removeAll(stream.toList());
    }
}
