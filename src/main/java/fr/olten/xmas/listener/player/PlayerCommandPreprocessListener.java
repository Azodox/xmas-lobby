package fr.olten.xmas.listener.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


/**
 * @author Azodox_ (Luke)
 * 22/5/2022.
 */

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().equalsIgnoreCase("/kill @e")
                || event.getMessage().equalsIgnoreCase("/kill @e[type=armor_stand]")
                || event.getMessage().equalsIgnoreCase("/kill @e[type=minecraft:armor_stand]")
                || event.getMessage().equalsIgnoreCase("/kill @e[type=falling_block]")
                || event.getMessage().equalsIgnoreCase("/kill @e[type=minecraft:falling_block]")) {
            event.getPlayer().sendMessage(Component.text("Espèce de fou, tu veux tuer tout le monde ?").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD));
            event.setCancelled(true);
        }
    }
}
