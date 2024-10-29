package fr.the__glacier.gcore.listener;

import fr.the__glacier.gcore.GCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){

        GCore.getInstance().userTable.UserJoin(event.getPlayer());

        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize("Hello <rainbow>World</rainbow>, isn't <underlined>MiniMessage</underlined> fun ?");
        Component parsed2 = mm.deserialize("<hover:show_text:\"<red>Bonjour ♥\">Ceci n'est qu'un</hover> test !");
        event.getPlayer().sendMessage(parsed);
        event.getPlayer().sendMessage(parsed2);
    }
}
