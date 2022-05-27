package fr.olten.xmas.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamageListener implements Listener {

    @EventHandler
    public void onFall(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            event.setCancelled(true);
        }
    }
}
