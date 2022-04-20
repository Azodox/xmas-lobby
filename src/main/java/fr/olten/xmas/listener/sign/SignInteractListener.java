package fr.olten.xmas.listener.sign;

import fr.olten.xmas.Lobby;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignInteractListener implements Listener {

    private final Lobby lobby;

    public SignInteractListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() == null) return;
        if(event.getAction().isRightClick()){
            if(event.getClickedBlock().getState() instanceof Sign sign){
                if(sign.getLocation().equals(lobby.getCarousel().getJoiningSign().getLocation())){
                    lobby.getCarousel().manager().ride(event.getPlayer());
                }
            }
        }
    }
}
