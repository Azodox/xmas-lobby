package fr.olten.xmas.listener.entity;

import fr.olten.xmas.Lobby;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


/**
 * @author Azodox_ (Luke)
 * 29/5/2022.
 */

public class EntityMoveListener implements Listener {

    private final Lobby lobby;

    public EntityMoveListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onMove(EntityMoveEvent event){
        if(lobby.getShootingRange().getGame() == null)
            return;

        if(event.getEntity() instanceof ArmorStand armorStand) {
            if (lobby.getShootingRange().getGame().getArmorStands().contains(armorStand)) {
                if (armorStand.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.RED_CONCRETE)) {
                    lobby.getShootingRange().getGame().standTouchedLine();
                }
            }
        }
    }
}
