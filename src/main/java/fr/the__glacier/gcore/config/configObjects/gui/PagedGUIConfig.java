package fr.the__glacier.gcore.config.configObjects.gui;

import org.bukkit.event.inventory.InventoryType;

public class PagedGUIConfig extends SimpleGUIConfig {
    public ItemGUIConfig previousPage;
    public ItemGUIConfig nextPage;

    public PagedGUIConfig(){
        super();
        this.previousPage = new ItemGUIConfig("minecraft", "arrow", "Previous", 1, null, null, this.size - 9);
        this.nextPage = new ItemGUIConfig("minecraft", "arrow", "Next", 1, null, null, this.size-1);
    }
    public PagedGUIConfig(String name, InventoryType inventoryType, int size, Background background, ItemGUIConfig previousPage, ItemGUIConfig nextPage){
        super(name, inventoryType, size, background);
        this.previousPage = previousPage;
        this.nextPage = nextPage;
    }
}
