package fr.the__glacier.gcore.gui;

import fr.the__glacier.gcore.config.configObjects.SimpleItemConfig;
import fr.the__glacier.gcore.config.configObjects.gui.BackGUIConfig;
import fr.the__glacier.gcore.config.configObjects.gui.Background;
import fr.the__glacier.gcore.config.configObjects.gui.ItemGUIConfig;
import fr.the__glacier.gcore.config.configObjects.gui.PagedGUIConfig;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PagedGUI extends SimpleGUI {
    public int page;
    public int maxPage;
    public ItemGUIConfig previous;
    public ItemGUIConfig next;
    public PagedGUI(PagedGUIConfig guiConfig) {
        super(guiConfig);
        this.page = 1;
        this.maxPage = Integer.MAX_VALUE;
        this.previous = guiConfig.previousPage;
        this.next = guiConfig.nextPage;
        this.items.put(13, new ItemGUI(previous, this::openGUI));
        ItemGUI previousItem = new ItemGUI(previous, this::setPrevious);
        ItemGUI nextItem = new ItemGUI(next, this::setNext);
        this.items.put(previous.slot, previousItem);
        this.items.put(next.slot, nextItem);
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = super.getInventory();
        inventory.setItem(13, this.items.get(13).config().getItem());
        if (this.page > 1) inventory.setItem(previous.slot, previous.getItem(Map.of("%page%", String.valueOf(page), "%maxPage%", String.valueOf(maxPage))));
        if (this.maxPage > this.page) inventory.setItem(next.slot, next.getItem(Map.of("%page%", String.valueOf(page), "%maxPage%", String.valueOf(maxPage))));
        return inventory;
    }

    public void setPrevious(InventoryClickEvent event){
        this.page -= 1;
        if (this.page < 1) this.page = 1;
        Inventory inventory = this.getInventory();
        event.getInventory().setContents(inventory.getContents());
    }

    public void setNext(InventoryClickEvent event){
        this.page += 1;
        if (this.page > this.maxPage) this.page = this.maxPage;
        Inventory inventory = this.getInventory();
        event.getInventory().setContents(inventory.getContents());
    }

    public void openGUI(InventoryClickEvent event){
        BackGUIConfig backGUIConfig = new BackGUIConfig("<green>Test hoyoyo ..................................................",
                InventoryType.CHEST,
                54,
                this.guiConfig.background,
                new ItemGUIConfig(
                "minecraft",
                "barrier",
                "<black><bold>Back",
                1,
                List.of("<green>Retour en arrière <red>♥"),
                new SimpleItemConfig.ItemOptions(null, true, null, false),
                18)
        );
        BackGUI backGUI = new BackGUI(backGUIConfig, event.getInventory());
        event.getWhoClicked().openInventory(backGUI.getInventory());
    }
}
