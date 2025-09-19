package fr.the__glacier.gcore.config.configObjects.gui;

import org.bukkit.event.inventory.InventoryType;

public class SimpleGUIConfig {
    public String name;
    public InventoryType type;
    public int size;
    public Background background;

    public SimpleGUIConfig(){
        this.name = "";
        this.type = InventoryType.CHEST;
        this.size = 27;
        this.background = new Background();
    }
    public SimpleGUIConfig(String name, InventoryType inventoryType, int size, Background background){
        this.name = name;
        this.type = inventoryType;
        this.size = size;
        this.background = background;
    }
}
