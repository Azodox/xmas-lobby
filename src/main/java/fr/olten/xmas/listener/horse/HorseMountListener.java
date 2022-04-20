package fr.olten.xmas.listener.horse;

import fr.olten.xmas.Lobby;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class HorseMountListener implements Listener {

    private final Lobby lobby;

    public HorseMountListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onMount(VehicleEnterEvent event) {
        if(event.getEntered() instanceof Player player){
            if(event.getVehicle() instanceof ArmorStand ride){
                if(lobby.getCarousel().isRide(ride)){
                    lobby.getCarousel().manager().ride(player, ride);
                }
            }else if(event.getVehicle() instanceof Horse horse){
                if(lobby.getCarousel().canBeRidden(horse)){
                    event.setCancelled(true);
                }
            }
        }
    }
}
