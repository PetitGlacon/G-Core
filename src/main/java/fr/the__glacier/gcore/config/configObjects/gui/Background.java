package fr.the__glacier.gcore.config.configObjects.gui;

import fr.the__glacier.gcore.config.configObjects.SimpleItemConfig;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Background {
    public SimpleItemConfig filler = null;
    public List<Integer> excludeSlots = null;
    public Map<Integer, SimpleItemConfig> items = null;

    public Background(){}

    public Background(SimpleItemConfig filler, List<Integer> excludeSlots, Map<Integer, SimpleItemConfig> items){
        this.filler = filler;
        this.excludeSlots = excludeSlots;
        this.items = items;
    }

    public void setBackground(Inventory inventory){
        ItemStack fillerItem = filler.getItem();
        for (int i = 0; i < inventory.getSize(); i++){
            if (items.containsKey(i)){
                inventory.setItem(i, items.get(i).getItem());
            } else if (!excludeSlots.contains(i)){
                inventory.setItem(i, fillerItem);
            }
        }
    }
}
