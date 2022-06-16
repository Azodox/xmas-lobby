package fr.olten.xmas.listener.entity;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private final Lobby lobby;

    public EntityDamageByEntityListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof NPC npc
                && lobby.getShootingRange().getGame() != null
                && lobby.getShootingRange().getGame().getNPCs().contains(npc)
                && lobby.getShootingRange().getGame().isRunning()){
            if(event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter){
                lobby.getShootingRange().getGame().kill(npc);
                lobby.getShootingRange().getGame().hasBeenKilled(npc, shooter);
            }
        }
    }
}
