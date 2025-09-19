package fr.the__glacier.gcore.config.configObjects;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ComplexItemConfig extends SimpleItemConfig {
    public Map<String, Integer> enchantments;

    @Override
    public ItemStack getItem(){
        ItemStack item = super.getItem();
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
        if (options.unbreakable){
            item.setData(DataComponentTypes.UNBREAKABLE);
        }

        return item;
    }
}
