package fr.the__glacier.gcore.config.configObjects.gui;

import fr.the__glacier.gcore.config.configObjects.SimpleItemConfig;

import java.util.List;

public class ItemGUIConfig extends SimpleItemConfig {
    public int slot;

    public ItemGUIConfig(String plugin, String material, String itemName, int amount, List<String> lore, ItemOptions options, int slot){
        super(plugin, material, itemName, amount, lore, options);
        this.slot = slot;
    }
}
