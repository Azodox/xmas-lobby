package fr.olten.xmas.listener.entity;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Azodox_ (Luke)
 * 22/5/2022.
 */

public class EntityDeathListener implements Listener {

    private final Lobby lobby;

    public EntityDeathListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof ArmorStand armorStand && lobby.getShootingRange().getGame() != null && lobby.getShootingRange().getGame().getArmorStands().contains(armorStand) && lobby.getShootingRange().getGame().isRunning()){
            lobby.getShootingRange().getGame().hasBeenKilled(armorStand, event.getEntity().getKiller());
            event.getDrops().clear();
        }
    }
}
