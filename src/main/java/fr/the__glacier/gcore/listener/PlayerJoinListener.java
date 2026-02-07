package fr.the__glacier.gcore.listener;

import fr.the__glacier.gcore.GCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent( PlayerJoinEvent event){

        GCore.getInstance().userTable.UserJoin(event.getPlayer());
    }
}
