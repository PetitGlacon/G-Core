package fr.the__glacier.gcore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PagedMessageManager {
    public Map<Plugin, Map<UUID, PagedMessage>> pagedMessages = new HashMap<>();

    public void addPagedMessage(JavaPlugin plugin, UUID uuid, PagedMessage pagedMessage){
        if (pagedMessages.containsKey(plugin)){
            Map<UUID, PagedMessage> messages = pagedMessages.get(plugin);
            messages.put(uuid, pagedMessage);
        } else {
            Map<UUID, PagedMessage> messages = new HashMap<>();
            messages.put(uuid, pagedMessage);
            pagedMessages.put(plugin, messages);
        }
    }
    public void addPagedMessage(JavaPlugin plugin, Map<UUID, PagedMessage> messagesToAdd){
        if (pagedMessages.containsKey(plugin)){
            Map<UUID, PagedMessage> messages = pagedMessages.get(plugin);
            messages.putAll(messagesToAdd);
        } else {
            pagedMessages.put(plugin, messagesToAdd);
        }
    }

    public PagedMessage getPagedMessage(Plugin plugin, UUID uuid){
        if (pagedMessages.containsKey(plugin) && pagedMessages.get(plugin).containsKey(uuid)){
            return pagedMessages.get(plugin).get(uuid);
        }
        return null;
    }
    public List<PagedMessage> getPagedMessages(){
        List<PagedMessage> messages = new ArrayList<>();
        pagedMessages.values().forEach(map -> messages.addAll(map.values()));
        return messages;
    }
}
