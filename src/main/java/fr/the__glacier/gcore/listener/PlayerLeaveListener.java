package fr.the__glacier.gcore.listener;

import fr.the__glacier.gcore.GCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event){
        GCore.getInstance().userTable.UserLeave(event.getPlayer());
    }
}
