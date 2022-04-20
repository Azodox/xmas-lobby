package fr.olten.xmas.listener.horse;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HorseGotDamagedListener implements Listener {

    private final Lobby lobby;
    public HorseGotDamagedListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Horse horse){
            if(lobby.getCarousel().canBeRidden(horse)){
                event.setCancelled(true);
            }
        }
    }
}
