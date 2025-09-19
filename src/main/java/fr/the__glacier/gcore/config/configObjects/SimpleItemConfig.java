package fr.the__glacier.gcore.config.configObjects;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.color.MiniMessages;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleItemConfig {
    public String plugin;
    public String material;
    public String itemName = null;
    public int amount = 1;
    public List<String> lore = null;
    public ItemOptions options = new ItemOptions();

    public SimpleItemConfig(){
        this.plugin = "minecraft";
        this.material = "stone";
    }
    public SimpleItemConfig(String plugin, String material, String itemName, int amount, List<String> lore, ItemOptions options){
        this.plugin = plugin;
        this.material = material;
        this.itemName = itemName;
        this.amount = amount;
        this.lore = lore;
        this.options = options;
    }


    public ItemStack getItem(){
        ItemStack item;
        if (this.plugin.equalsIgnoreCase("minecraft")){
            Material material = Material.matchMaterial(this.material.toUpperCase());
            if (material == null) return air();
            item = new ItemStack(material, amount);
        } else {
            return air();
        }
        if (lore != null && !lore.isEmpty()){
            List<Component> l = new ArrayList<>();
            for (String str : lore){
                l.add(new MiniMessages(str).getComponent());
            }
            item.lore(l);
        }
        if (itemName != null) item.setData(DataComponentTypes.CUSTOM_NAME, new MiniMessages(itemName).getComponent());
        if (options != null){
            if (options.glint != null) {
                if (options.glint) {
                    item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                } else {
                    item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
                }
            }
            if (options.itemFlags != null){
                item.addItemFlags(options.itemFlags);
            }
            if (options.customModelData != null){
                CustomModelData customModelData = CustomModelData.customModelData()
                        .addFloat(options.customModelData)
                        .build();
                item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customModelData);
            }
        }
        return item;
    }
    public ItemStack getItem(Map<String, String> placeholders){
        if (placeholders == null) return getItem();
        ItemStack item;
        if (this.plugin.equalsIgnoreCase("minecraft")){
            Material material = Material.matchMaterial(this.material.toUpperCase());
            if (material == null) return air();
            item = new ItemStack(material, amount);
        } else {
            return air();
        }
        if (lore != null && !lore.isEmpty()){
            List<Component> l = new ArrayList<>();
            for (String str : lore){
                l.add(getComponent(str, placeholders));
            }
            item.lore(l);
        }
        if (itemName != null) item.setData(DataComponentTypes.CUSTOM_NAME, getComponent(itemName, placeholders));
        if (options != null){
            if (options.glint != null) {
                if (options.glint) {
                    item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                } else {
                    item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
                }
            }
            if (options.itemFlags != null){
                item.addItemFlags(options.itemFlags);
            }
            if (options.customModelData != null){
                CustomModelData customModelData = CustomModelData.customModelData()
                        .addFloat(options.customModelData)
                        .build();
                item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customModelData);
            }
        }
        return item;
    }
    public Component getComponent(String str, Map<String, String> placeholders){
        for (Map.Entry<String, String> entry : placeholders.entrySet()){
            str = str.replace(entry.getKey(), entry.getValue());
        }
        MiniMessages miniMessages = new MiniMessages(str);
        return miniMessages.getComponent();
    }
    public ItemStack air(){
        return new ItemStack(Material.AIR);
    }

    public static class ItemOptions{
        public Integer customModelData = null;
        public Boolean glint = null;
        public ItemFlag[] itemFlags = null;

        public boolean unbreakable = false;

        public ItemOptions(){}
        public ItemOptions(Integer customModelData, Boolean glint, ItemFlag[] itemFlags){
            this.customModelData = customModelData;
            this.glint = glint;
            this.itemFlags = itemFlags;
        }
        public ItemOptions(Integer customModelData, Boolean glint, ItemFlag[] itemFlags, boolean unbreakable){
            this.customModelData = customModelData;
            this.glint = glint;
            this.itemFlags = itemFlags;

            this.unbreakable = unbreakable;
        }
    }
}
