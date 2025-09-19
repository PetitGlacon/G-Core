package fr.the__glacier.gcore.listener;

import fr.the__glacier.gcore.gui.SimpleGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getInventory().getHolder() instanceof SimpleGUI holder){
            holder.onClickEvent(event);
        }
    }
}
