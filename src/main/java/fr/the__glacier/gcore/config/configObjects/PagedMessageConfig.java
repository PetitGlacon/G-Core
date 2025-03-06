package fr.the__glacier.gcore.config.configObjects;

import fr.the__glacier.gcore.util.PagedMessage;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PagedMessageConfig {

    public final List<String> message;
    public final List<String> top;
    public final String bottom;
    public final PagedMessage.BottomType bottomType;
    public Map<PagedMessage.BottomTypePlaceholders, String> pagedNumberPlaceholders;
    public final int nbLines;

    public PagedMessageConfig(){
        this.message = null;
        this.top = null;
        this.bottom = null;
        this.bottomType = null;
        this.nbLines = 1;
        this.pagedNumberPlaceholders = new HashMap<>();
    }
    public PagedMessageConfig(List<String> message, List<String> top, String bottom, PagedMessage.BottomType bottomType, Map<PagedMessage.BottomTypePlaceholders, String> pageNumberPlaceholders, int nbLines){
        this.message = message;
        this.top = top;
        this.bottom = bottom;
        this.bottomType = bottomType;
        this.pagedNumberPlaceholders = pageNumberPlaceholders;
        this.nbLines = nbLines;
    }
    public PagedMessage getPagedMessage(UUID uuid, Plugin plugin){
        assert this.message != null;
        return new PagedMessage(this.message, this.top, this.bottom, this.bottomType, this.pagedNumberPlaceholders, this.nbLines, uuid, plugin);
    }
}
