package fr.the__glacier.gcore.gui;

import fr.the__glacier.gcore.color.MiniMessages;
import fr.the__glacier.gcore.config.configObjects.gui.SimpleGUIConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleGUI implements InventoryHolder {
    public SimpleGUIConfig guiConfig;
    public Map<Integer, ItemGUI> items = new HashMap<>();

    public SimpleGUI(SimpleGUIConfig guiConfig){
        this.guiConfig = guiConfig;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv;
        if (guiConfig.type == InventoryType.CHEST){
            inv = Bukkit.createInventory(this, guiConfig.size, new MiniMessages(guiConfig.name).getComponent());
        } else {
            inv = Bukkit.createInventory(this, guiConfig.type, new MiniMessages(guiConfig.name).getComponent());
        }
        guiConfig.background.setBackground(inv);

        return inv;
    }
    public void onClickEvent(InventoryClickEvent event){
        if (event.isCancelled()) return;
        event.setCancelled(true);
        ItemGUI itemGUI = items.get(event.getSlot());
        if (itemGUI == null) return;
        Consumer<InventoryClickEvent> consumer = itemGUI.action();
        if (consumer == null) return;
        consumer.accept(event);
    }
}
