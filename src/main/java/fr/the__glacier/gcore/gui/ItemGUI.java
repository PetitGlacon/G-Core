package fr.the__glacier.gcore.gui;

import fr.the__glacier.gcore.config.configObjects.gui.ItemGUIConfig;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public record ItemGUI(ItemGUIConfig config, Consumer<InventoryClickEvent> action){

}
