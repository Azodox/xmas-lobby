package fr.olten.xmas.listener.player;

import fr.olten.xmas.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Lobby lobby;

    public PlayerQuitListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        var player = event.getPlayer();
        if(lobby.getCarousel().manager().isRiding(player)){
            lobby.getCarousel().manager().dismount(player);
        }

        if(lobby.getShootingRange().getPlayers().contains(player)){
            lobby.getShootingRange().leave(player);
        }
    }
}
