package fr.the__glacier.gcore.test;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.gui.SimpleGUI;
import fr.the__glacier.gcore.listener.PlayerJoinListener;
import fr.the__glacier.gcore.util.PlayerUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize("Hello <rainbow>World</rainbow>, isn't <underlined>MiniMessage</underlined> fun ?");
        Component parsed2 = mm.deserialize("<hover:show_text:\"<red>Bonjour ♥\">Ceci n'est qu'un</hover> test !");
        event.getPlayer().sendMessage(parsed);
        event.getPlayer().sendMessage(parsed2);
    }
}
