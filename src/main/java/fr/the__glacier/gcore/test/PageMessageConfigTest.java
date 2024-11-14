package fr.the__glacier.gcore.test;

import fr.the__glacier.gcore.GCore;
import fr.the__glacier.gcore.util.PageMessages;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PageMessageConfigTest {

    public final List<String> message;
    public final List<String> top;
    public final String bottom;
    public final PageMessages.BottomType bottomType;
    public Map<PageMessages.BottomTypePlaceholders, String> pageNumberPlaceholders;
    public final int nbLines;

    public PageMessageConfigTest(){
        this.message = null;
        this.top = null;
        this.bottom = null;
        this.bottomType = null;
        this.nbLines = 1;
        this.pageNumberPlaceholders = new HashMap<>();
    }
    public PageMessageConfigTest(List<String> message, List<String> top, String bottom, PageMessages.BottomType bottomType, Map<PageMessages.BottomTypePlaceholders, String> pageNumberPlaceholders, int nbLines){
        this.message = message;
        this.top = top;
        this.bottom = bottom;
        this.bottomType = bottomType;
        this.pageNumberPlaceholders = pageNumberPlaceholders;
        this.nbLines = nbLines;
    }
    public PageMessages getPageMessage(UUID uuid, Plugin plugin){
        assert this.message != null;
        return new PageMessages(this.message, this.top, this.bottom, this.bottomType, this.pageNumberPlaceholders, this.nbLines, uuid, plugin);
    }
}
