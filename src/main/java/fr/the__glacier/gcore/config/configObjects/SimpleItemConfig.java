package fr.the__glacier.gcore.config.configObjects;

import fr.the__glacier.gcore.color.MiniMessages;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SimpleItemConfig {
    public String plugin;
    public String material;
    public String itemName;
    public int amount;
    public List<String> lore;
    public Map<String, Integer> enchantments;
    public ItemOptions options;


    public ItemStack getItem(){
        ItemStack item;
        if (this.plugin.equalsIgnoreCase ("minecraft:")){
            Material material = Material.matchMaterial(this.material.toUpperCase());
            if (material == null) return air();
            item = new ItemStack(material, amount);
        } else {
            return air();
        }
        if (enchantments != null){
            Registry<@NotNull Enchantment> enchantmentsRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
            for (Map.Entry<String, Integer> enchant : enchantments.entrySet()){
                NamespacedKey key = NamespacedKey.fromString(enchant.getKey());
                if (key == null) continue;
                Enchantment enchantment = enchantmentsRegistry.get(key);
                if (enchantment == null) continue;
                item.addUnsafeEnchantment(enchantment, enchant.getValue());
            }
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemName != null) itemMeta.itemName(new MiniMessages(itemName).getComponent());
        if (options != null){
            itemMeta.addItemFlags(options.itemFlags);
            itemMeta.setUnbreakable(options.unbreakable);
            itemMeta.setCustomModelData(options.customModelData);
        }
        item.setItemMeta(itemMeta);
        return item;
    }
    public ItemStack air(){
        return new ItemStack(Material.AIR);
    }

    public static class ItemOptions{
        public boolean unbreakable;
        public int customModelData;
        public ItemFlag[] itemFlags;
    }
}
