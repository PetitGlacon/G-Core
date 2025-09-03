package fr.the__glacier.gcore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandCompletionListener implements Listener {
    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event){
        event.getCommands().remove("gcoreutils");
    }
}
