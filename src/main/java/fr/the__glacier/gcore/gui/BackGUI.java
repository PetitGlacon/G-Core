package fr.the__glacier.gcore.gui;

import fr.the__glacier.gcore.config.configObjects.gui.BackGUIConfig;
import fr.the__glacier.gcore.config.configObjects.gui.ItemGUIConfig;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class BackGUI extends SimpleGUI {
    public Inventory previousInv;
    public ItemGUIConfig back;

    public BackGUI(BackGUIConfig guiConfig, Inventory backInv) {

        super(guiConfig);
        this.previousInv = backInv;
        this.back = guiConfig.backItem;
        this.items.put(guiConfig.backItem.slot, new ItemGUI(guiConfig.backItem, event -> {
            event.getWhoClicked().openInventory(previousInv);
            event.getInventory().close();
        }));
    }
    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = super.getInventory();
        inventory.setItem(back.slot, back.getItem());
        return inventory;
    }
}