package fr.the__glacier.gcore.config.configObjects.gui;

import org.bukkit.event.inventory.InventoryType;

public class BackGUIConfig extends SimpleGUIConfig {
    public ItemGUIConfig backItem;

    public BackGUIConfig(){
        super();
        this.backItem = new ItemGUIConfig("minecraft", "arrow", "Back", 1, null, null, this.size - 5);
    }
    public BackGUIConfig(String name, InventoryType inventoryType, int size, Background background, ItemGUIConfig backItem){
        super(name, inventoryType, size, background);
        this.backItem = backItem;
    }

}
