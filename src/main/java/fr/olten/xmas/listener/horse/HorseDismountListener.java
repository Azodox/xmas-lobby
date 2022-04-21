package fr.olten.xmas.listener.horse;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class HorseDismountListener implements Listener {

    private final Lobby lobby;
    public HorseDismountListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event){
        if(event.getEntity() instanceof Player player){
            if(event.getDismounted() instanceof ArmorStand stand){
                if(lobby.getCarousel().isRide(stand)){
                    stand.removePassenger(player);
                    lobby.getCarousel().manager().dismount(player);
                }
            }
        }
    }
}