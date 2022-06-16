package fr.olten.xmas.listener.entity;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.NPC;
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
        if(event.getEntity() instanceof NPC npc && lobby.getShootingRange().getGame() != null && lobby.getShootingRange().getGame().getNPCs().contains(npc) && lobby.getShootingRange().getGame().isRunning()){
            event.getDrops().clear();
        }
    }
}
