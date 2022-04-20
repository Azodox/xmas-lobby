package fr.olten.xmas.listener;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.manager.TeamNameTagManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Lobby lobby;

    public PlayerJoinListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if(player.isInsideVehicle()) {
            if(lobby.getCarousel().isRide(player.getVehicle())) {
                lobby.getCarousel().manager().dismount(player);
            }
        }
        TeamNameTagManager.update(player);
    }
}
